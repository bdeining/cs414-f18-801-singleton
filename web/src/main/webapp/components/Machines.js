import React, { Component } from "react";
import axios from "axios";
import ReactTable from "react-table";
import "react-table/react-table.css";
import "./styles.css";
import Modal from "./Modal";

class Machine extends React.Component {
  constructor() {
    super();
    this.state = {
      data: [],
      show: false,
      id: "",
      selected: null,
      add: false,
      selectedFile: ""
    };
  }

  showAddModal = () => {
    this.setState({
      add: true
    });
    this.showModal();
  };

  clearMachineState = () => {
    this.setState({
      quantity: "",
      picture: "",
      name: "",
      id: "",
      add: false,
      show: false,
      selectedFile: ""
    });
  };

  showModal = () => {
    this.setState({
      show: true,
      selectedFile: ""
    });
  };

  hideModal = () => {
    this.clearMachineState();
  };

  deleteMachine = () => {
    axios({
      method: "delete",
      url: "/services/rest/machine?id=" + this.state.id
    }).then(res => {
      this.getMachineData();
      this.clearMachineState();
    });
  };

  saveModal = () => {
    var that = this;
    if (this.state.id) {
      if (this.state.selectedFile) {
        var reader = new FileReader();

        reader.readAsDataURL(this.state.selectedFile);
        reader.onload = function() {
          axios({
            method: "put",
            url: "/services/rest/machine",
            data: {
              name: that.state.name,
              picture: reader.result,
              quantity: that.state.quantity,
              id: that.state.id
            }
          }).then(res => {
            that.getMachineData();
            that.clearMachineState();
          });
        };
      } else {
        axios({
          method: "put",
          url: "/services/rest/machine",
          data: {
            name: that.state.name,
            picture: "",
            quantity: that.state.quantity,
            id: that.state.id
          }
        }).then(res => {
          that.getMachineData();
          that.clearMachineState();
        });
      }
    } else {
      var reader = new FileReader();
      reader.readAsDataURL(this.state.selectedFile);
      reader.onload = function() {
        axios({
          method: "put",
          url: "/services/rest/machine",
          data: {
            name: that.state.name,
            picture: reader.result,
            quantity: that.state.quantity
          }
        }).then(res => {
          that.getMachineData();
          that.clearMachineState();
        });
      };
    }
  };

  getMachineData() {
    axios.get("/services/rest/machine").then(res => {
      this.setState({
        data: [...this.state.data]
      });

      this.setState({
        data: res.data,
        id: "",
        show: this.state.show
      });
    });
  }

  componentDidMount() {
    this.getMachineData();
  }

  updateInputValue = evt => {
    this.setState({
      [evt.target.name]: evt.target.value
    });
  };

  fileChangedHandler = event => {
    this.setState({ selectedFile: event.target.files[0] });
  };

  render() {
    return (
      <div>
        <Modal
          show={this.state.show}
          handleClose={this.hideModal}
          handleSave={this.saveModal}
          handleDelete={this.deleteMachine}
          add={this.state.add}
          manipulated={false}
        >
          <h1>Machine</h1>
          <div>
            <label>Name</label>
            <input
              name="name"
              value={this.state.name}
              onChange={this.updateInputValue}
            />
          </div>
          <div>
            <label>Quantity</label>
            <input
              name="quantity"
              value={this.state.quantity}
              onChange={this.updateInputValue}
            />
          </div>
          <div>
            <label>File</label>
            <input type="file" onChange={this.fileChangedHandler} />
          </div>
          <div>
            <label>ID</label>
            <input name="id" value={this.state.id} readOnly />
          </div>
        </Modal>
        <button type="button" onClick={this.showAddModal}>
          Add
        </button>
        <ReactTable
          data={this.state.data}
          columns={[
            {
              Header: "Name",
              accessor: "name"
            },
            {
              Header: "Picture",
              accessor: "picture",
              Cell: row => <img id="base64image" src={row.value} />
            },
            {
              Header: "Quantity",
              accessor: "quantity"
            },
            {
              Header: "ID",
              accessor: "id"
            }
          ]}
          defaultPageSize={10}
          className="-striped -highlight"
          getTrProps={(state, rowInfo) => {
            if (rowInfo && rowInfo.row) {
              return {
                onClick: e => {
                  this.setState({
                    name: rowInfo.row.name,
                    picture: rowInfo.row.picture,
                    quantity: rowInfo.row.quantity,
                    id: rowInfo.row.id,
                    selected: rowInfo.index
                  });
                  this.showModal();
                },
                style: {
                  background:
                    rowInfo.index === this.state.selected ? "#00afec" : "white",
                  color:
                    rowInfo.index === this.state.selected ? "white" : "black"
                }
              };
            } else {
              return {};
            }
          }}
        />
      </div>
    );
  }
}

export default Machine;
