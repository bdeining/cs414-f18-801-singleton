import React from "react";

const Modal = ({
  handleClose,
  handleSave,
  handleDelete,
  show,
  add,
  children
}) => {
  const showHideClassName = show ? "modal display-block" : "modal display-none";
  const showDeleteButton = add;
  return (
    <div className={showHideClassName}>
      <section className="modal-main">
        {children}
        <button onClick={handleClose}>Close</button>
        <button onClick={handleDelete} disabled={showDeleteButton}>
          Delete
        </button>
        <button onClick={handleSave}>Save</button>
      </section>
    </div>
  );
};

export default Modal;
