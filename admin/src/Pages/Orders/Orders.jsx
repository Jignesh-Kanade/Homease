import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import "./Orders.css";

const Orders = ({ url }) => {
  const [orders, setOrders] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [statusNote, setStatusNote] = useState('');

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await axios.get(`${url}/api/orders`);
      setOrders(response.data);
    } catch (error) {
      console.error("Error fetching orders:", error);
    }
  };

  const updateStatus = async (id, newStatus) => {
    try {
      const response = await axios.put(
        `${url}/api/orders/${id}/status`,
        { status: newStatus }
      );
      if (response.status === 200) {
        setOrders((prevOrders) =>
          prevOrders.map((order) =>
            order._id === id ? { ...order, status: newStatus } : order
          )
        );
      }
    } catch (error) {
      console.error(
        "Error updating status:",
        error.response?.data || error.message
      );
    }
  };

  const updateTrackingStatus = async (id, trackingStatus) => {
    // If id passed is undefined, try to fallback to selectedOrder id
    const orderId = id || (selectedOrder ? (selectedOrder._id || selectedOrder.id) : null);

    if (!orderId) {
      console.error("Order ID is missing for update");
      return;
    }

    try {
      const response = await axios.put(
        `${url}/api/orders/${orderId}/status`,
        { trackingStatus, notes: statusNote || `Order status updated to ${trackingStatus}` }
      );
      if (response.status === 200) {
        setOrders((prevOrders) =>
          prevOrders.map((order) =>
            (order._id === orderId || order.id === orderId) ? { ...order, trackingStatus } : order
          )
        );
        setStatusNote('');
        if (selectedOrder && (selectedOrder._id === orderId || selectedOrder.id === orderId)) {
          // Merge response data to keep other fields but update what changed
          setSelectedOrder(prev => ({ ...prev, ...response.data, trackingStatus }));
        }
      }
    } catch (error) {
      console.error(
        "Error updating tracking status:",
        error.response?.data || error.message
      );
    }
  };

  const openOrderDetails = (order) => {
    setSelectedOrder(order);
    setStatusNote('');
  };

  const closeOrderDetails = () => {
    setSelectedOrder(null);
    setStatusNote('');
  };

  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  return (
    <div className="orders">
      <h2>Orders</h2>
      {orders.length === 0 ? (
        <p>No orders found</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Status</th>
              <th>Date</th>
              <th>Services</th>
              <th>Customer</th>
              <th>Email</th>
              <th>Phone</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order) => (
              <tr key={order._id || order.id}>
                <td>
                  <span className={`tracking-status ${order.trackingStatus || 'order_placed'}`}>
                    {(order.trackingStatus || 'order_placed').replace(/_/g, ' ').toUpperCase()}
                  </span>
                </td>
                <td>
                  {order.serviceDate || (order.userDetails ? order.userDetails.serviceDate : "N/A")}
                </td>
                <td>
                  {(order.orderItems || order.services || []).length > 0
                    ? (order.orderItems || order.services)
                      .map((item) =>
                        item.name || item.serviceId || "Unknown"
                      )
                      .join(", ")
                    : "No Services"}
                </td>
                <td>{`${order.firstName || order.userDetails?.firstName || "N/A"} ${order.lastName || order.userDetails?.lastName || ""
                  }`}</td>
                <td>{order.email || order.userDetails?.email || "N/A"}</td>
                <td>{order.phone || order.userDetails?.phone || "N/A"}</td>
                <td>
                  <button
                    className="view-details-btn"
                    onClick={() => openOrderDetails(order)}
                  >
                    View Details
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {selectedOrder && (
        <div className="order-modal-overlay">
          <div className="order-modal">
            <button className="close-button" onClick={closeOrderDetails}>&times;</button>
            <h2>Order #{selectedOrder.orderId}</h2>

            <div className="order-details-grid">
              <div className="order-info-section">
                <h3>Order Information</h3>
                <p><strong>Order Date:</strong> {formatDate(selectedOrder.createdAt)}</p>
                <p><strong>Service Date:</strong> {selectedOrder.serviceDate || selectedOrder.userDetails?.serviceDate || "N/A"}</p>
                <p><strong>Amount:</strong> ₹{selectedOrder.amount}</p>
              </div>

              <div className="customer-info-section">
                <h3>Customer Information</h3>
                <p><strong>Name:</strong> {selectedOrder.firstName || selectedOrder.userDetails?.firstName} {selectedOrder.lastName || selectedOrder.userDetails?.lastName}</p>
                <p><strong>Email:</strong> {selectedOrder.email || selectedOrder.userDetails?.email}</p>
                <p><strong>Phone:</strong> {selectedOrder.phone || selectedOrder.userDetails?.phone}</p>
                <p><strong>Address:</strong> {selectedOrder.street || selectedOrder.userDetails?.street}, {selectedOrder.city || selectedOrder.userDetails?.city}, {selectedOrder.state || selectedOrder.userDetails?.state} - {selectedOrder.pincode || selectedOrder.userDetails?.pincode}</p>
              </div>
            </div>

            <div className="services-section">
              <h3>Services Ordered</h3>
              <ul className="services-list">
                {(selectedOrder.orderItems || selectedOrder.services || []).length > 0 ?
                  (selectedOrder.orderItems || selectedOrder.services).map((item, index) => (
                    <li key={index}>{item.name || item.serviceId || "Unknown Service"}</li>
                  ))
                  : <li>No services found</li>
                }
              </ul>
            </div>

            <div className="tracking-update-section">
              <h3>Update Tracking Status</h3>
              <div className="tracking-form">
                <div className="status-options">
                  <div className="status-option-row">
                    {['order_placed', 'confirmed', 'processing', 'dispatched'].map(status => (
                      <button
                        key={status}
                        className={`status-btn ${selectedOrder.trackingStatus === status ? 'active' : ''}`}
                        onClick={() => updateTrackingStatus(selectedOrder._id || selectedOrder.id, status)}
                      >
                        {status.replace(/_/g, ' ').toUpperCase()}
                      </button>
                    ))}
                  </div>
                  <div className="status-option-row">
                    {['out_for_delivery', 'delivered', 'cancelled'].map(status => (
                      <button
                        key={status}
                        className={`status-btn ${selectedOrder.trackingStatus === status ? 'active' : ''}`}
                        onClick={() => updateTrackingStatus(selectedOrder._id || selectedOrder.id, status)}
                      >
                        {status.replace(/_/g, ' ').toUpperCase()}
                      </button>
                    ))}
                  </div>
                </div>
                <div className="status-notes">
                  <label htmlFor="status-note">Add a note with this status update:</label>
                  <textarea
                    id="status-note"
                    value={statusNote}
                    onChange={(e) => setStatusNote(e.target.value)}
                    placeholder="Optional: Add details about this status change"
                    rows={3}
                  ></textarea>
                </div>
              </div>
            </div>

            <div className="tracking-history-section">
              <h3>Tracking History</h3>
              {selectedOrder.trackingHistory && selectedOrder.trackingHistory.length > 0 ? (
                <div className="tracking-timeline">
                  {selectedOrder.trackingHistory.map((entry, index) => (
                    <div key={index} className="timeline-item">
                      <div className="timeline-marker"></div>
                      <div className="timeline-content">
                        <div className="timeline-date">{formatDate(entry.timestamp)}</div>
                        <div className="timeline-status">{entry.status.replace(/_/g, ' ').toUpperCase()}</div>
                        <div className="timeline-notes">{entry.notes}</div>
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <p>No tracking history available</p>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Orders;
