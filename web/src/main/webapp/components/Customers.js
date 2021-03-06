import React, { Component } from "react";
import axios from "axios";
import ReactTable from "react-table";
import "react-table/react-table.css";
import "./styles.css";
import Select from "react-select";
import Modal from "./Modal";

const activityOptions = [
  { value: "ACTIVE", label: "ACTIVE" },
  { value: "INACTIVE", label: "INACTIVE" }
];

class Customer extends React.Component {
  constructor() {
    super();
    this.state = {
      firstName: "",
      lastName: "",
      address: "",
      phone: "",
      email: "",
      healthInsuranceProvider: "",
      activity: "",
      id: "",
      data: [],
      routineNames: [],
      activity: activityOptions[0],
      show: false,
      add: false,
      branch: localStorage.getItem("branch"),
      selected: null
    };
    axios({
      method: "get",
      url: "/services/rest/routinenames?branch=" + this.state.branch
    }).then(res => {
      this.setState({
        routineNames: res.data
      });
    });
  }

  clearCustomerState = () => {
    this.setState({
      firstName: "",
      lastName: "",
      address: "",
      phone: "",
      email: "",
      healthInsuranceProvider: "",
      id: "",
      add: false,
      workoutRoutineIds: [],
      show: false
    });
  };

  showAddModal = () => {
    this.setState({
      add: true
    });
    this.showModal();
  };

  showModal = () => {
    axios({
      method: "get",
      url: "/services/rest/routinenames?branch=" + this.state.branch
    }).then(res => {
      this.setState({
        show: true,
        routineNames: res.data
      });
    });
  };

  hideModal = () => {
    this.clearCustomerState();
  };

  deleteCustomer = () => {
    axios({
      method: "delete",
      url: "/services/rest/customer?id=" + this.state.id
    }).then(res => {
      this.getCustomerData();
      this.clearCustomerState();
    });
  };

  validate = () => {
    return {
      address: this.state.address.length === 0,
      firstName: this.state.firstName.length === 0,
      lastName: this.state.lastName.length === 0,
      phone: this.state.phone.length === 0,
      email: this.state.email.length === 0,
      healthInsuranceProvider: this.state.healthInsuranceProvider.length === 0
    };
  };

  canBeSaved = () => {
    const errors = this.validate();
    const isDisabled = Object.keys(errors).some(x => errors[x]);
    return !isDisabled;
  };

  saveModal = () => {
    if (!this.canBeSaved()) {
      return;
    }

    var routineIds = [];

    if (this.state.workoutRoutineIds) {
      routineIds = this.state.workoutRoutineIds.map(function(item) {
        return item["value"];
      });
    }

    if (this.state.id) {
      axios({
        method: "put",
        url: "/services/rest/customer",
        data: {
          address: this.state.address,
          firstName: this.state.firstName,
          lastName: this.state.lastName,
          phone: this.state.phone,
          email: this.state.email,
          healthInsuranceProvider: this.state.healthInsuranceProvider,
          activity: this.state.activity.value,
          branch: this.state.branch,
          id: this.state.id,
          workoutRoutineIds: routineIds
        }
      }).then(res => {
        this.getCustomerData();
      });
    } else {
      axios({
        method: "put",
        url: "/services/rest/customer",
        data: {
          address: this.state.address,
          firstName: this.state.firstName,
          lastName: this.state.lastName,
          phone: this.state.phone,
          email: this.state.email,
          healthInsuranceProvider: this.state.healthInsuranceProvider,
          activity: this.state.activity.value,
          branch: this.state.branch,
          workoutRoutineIds: routineIds
        }
      }).then(res => {
        this.getCustomerData();
      });
    }

    this.clearCustomerState();
  };

  getCustomerData() {
    axios
      .get("/services/rest/customer?branch=" + this.state.branch)
      .then(res => {
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
    this.getCustomerData();
  }

  updateInputValue = evt => {
    this.setState({
      [evt.target.name]: evt.target.value
    });
  };

  handleRoutineChange = selectedOption => {
    this.setState({ workoutRoutineIds: selectedOption });
  };

  handleActivityChange = selectedOption => {
    this.setState({ activity: selectedOption });
  };

  render() {
    const errors = this.validate();
    const isDisabled = Object.keys(errors).some(x => errors[x]);
    return (
      <div>
        <Modal
          show={this.state.show}
          handleClose={this.hideModal}
          handleSave={this.saveModal}
          handleDelete={this.deleteCustomer}
          add={this.state.add}
          manipulated={false}
        >
          <h1>Customer</h1>
          <div>
            <label>First Name</label>
            <input
              className={errors.firstName ? "error" : ""}
              name="firstName"
              value={this.state.firstName}
              onChange={this.updateInputValue}
            />
          </div>
          <div>
            <label>Last Name</label>
            <input
              className={errors.lastName ? "error" : ""}
              name="lastName"
              value={this.state.lastName}
              onChange={this.updateInputValue}
            />
          </div>
          <div>
            <label>Address</label>
            <input
              className={errors.address ? "error" : ""}
              name="address"
              value={this.state.address}
              onChange={this.updateInputValue}
            />
          </div>
          <div>
            <label>Phone</label>
            <input
              className={errors.phone ? "error" : ""}
              name="phone"
              value={this.state.phone}
              onChange={this.updateInputValue}
            />
          </div>
          <div>
            <label>Email</label>
            <input
              className={errors.email ? "error" : ""}
              name="email"
              value={this.state.email}
              onChange={this.updateInputValue}
            />
          </div>
          <div>
            <label>Health Insurance Provider</label>
            <input
              className={errors.healthInsuranceProvider ? "error" : ""}
              name="healthInsuranceProvider"
              value={this.state.healthInsuranceProvider}
              onChange={this.updateInputValue}
            />
          </div>
          <div>
            <label>Activity</label>
            <div className="react-select-container">
              <Select
                value={this.state.activity}
                onChange={this.handleActivityChange}
                options={activityOptions}
              />
            </div>
          </div>
          <div>
            <label>Workout Routines</label>
            <div className="react-select-container">
              <Select
                isMulti
                closeMenuOnSelect={false}
                value={this.state.workoutRoutineIds}
                onChange={this.handleRoutineChange}
                options={this.state.routineNames}
              />
            </div>
          </div>
          <div>
            <label>ID</label>
            <input name="id" value={this.state.id} readOnly />
          </div>
        </Modal>
        <button type="button" className="add" onClick={this.showAddModal}>
          Add
        </button>
        <ReactTable
          data={this.state.data}
          columns={[
            {
              Header: "First Name",
              accessor: "firstName"
            },
            {
              Header: "Last Name",
              accessor: "lastName"
            },
            {
              Header: "Address",
              accessor: "address"
            },
            {
              Header: "Phone",
              accessor: "phone"
            },
            {
              Header: "Email",
              accessor: "email"
            },
            {
              Header: "Activity",
              accessor: "activity"
            },
            {
              Header: "Health Insurance Provider",
              accessor: "healthInsuranceProvider"
            },
            {
              Header: "Workout Routines",
              accessor: "workoutRoutineIds",
              Cell: row => <span>{row.value.join(", ")}</span>
            },
            {
              Header: "ID",
              accessor: "id"
            }
          ]}
          defaultPageSize={50}
          showPaginationBottom={false}          defaultPageSize={50}
          showPaginationBottom={false}
          className="-striped -highlight"
          getTrProps={(state, rowInfo) => {
            if (rowInfo && rowInfo.row) {
              var selectedOption;
              for (var i = 0; i < activityOptions.length; i++) {
                if (activityOptions[i].value === rowInfo.row.activity) {
                  selectedOption = activityOptions[i];
                }
              }

              var workoutRoutineList = [];
              for (var i = 0; i < this.state.routineNames.length; i++) {
                for (var c = 0; c < rowInfo.row.workoutRoutineIds.length; c++) {
                  if (
                    this.state.routineNames[i].value ===
                    rowInfo.row.workoutRoutineIds[c]
                  ) {
                    workoutRoutineList.push(this.state.routineNames[i]);
                  }
                }
              }

              return {
                onClick: e => {
                  this.setState({
                    firstName: rowInfo.row.firstName,
                    lastName: rowInfo.row.lastName,
                    address: rowInfo.row.address,
                    email: rowInfo.row.email,
                    phone: rowInfo.row.phone,
                    healthInsuranceProvider:
                      rowInfo.row.healthInsuranceProvider,
                    activity: selectedOption,
                    workoutRoutineIds: workoutRoutineList,
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

export default Customer;
