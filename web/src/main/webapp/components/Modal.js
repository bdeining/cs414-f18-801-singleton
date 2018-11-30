import React from "react";

const Modal = ({
  handleClose,
  handleSave,
  handleDelete,
  show,
  add,
  manipulated,
  children
}) => {
  const showHideClassName = show ? "modal display-block" : "modal display-none";
  const showDeleteButton = add;
  const editable = manipulated;
  return (
    <div className={showHideClassName}>
      <section className="modal-main">
        {children}
        <button onClick={handleClose}>Close</button>
        <button onClick={handleDelete} disabled={manipulated || showDeleteButton}>
          Delete
        </button>
        <button onClick={handleSave} disabled={manipulated}>Save</button>
      </section>
    </div>
  );
};

export default Modal;
