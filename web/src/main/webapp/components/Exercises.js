import React, { Component } from "react";
import axios from "axios";
import ReactTable from "react-table";
import "react-table/react-table.css";
import Select from "react-select";
import "./styles.css";
import Modal from "./Modal";

class Exercise extends React.Component {
  constructor() {
    super();
    this.state = {
      data: [],
      machineNames: [],
      show: false,
      isManager: localStorage.getItem("user") === "manager",
      commonName: "",
      machineId: "",
      sets: "",
      add: false,
      durationPerSet: "",
      id: "",
      branch: localStorage.getItem("branch"),
      selected: null
    };
    axios({
      method: "get",
      url: "/services/rest/machinenames?branch=" + this.state.branch
    }).then(res => {
      this.setState({
        machineNames: res.data
      });
    });
  }

  clearExerciseState = () => {
    this.setState({
      commonName: "",
      machineId: "",
      sets: "",
      add: false,
      durationPerSet: "",
      id: "",
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
      url: "/services/rest/machinenames?branch=" + this.state.branch
    }).then(res => {
      this.setState({
        show: true,
        machineNames: res.data
      });
    });
  };

  hideModal = () => {
    this.clearExerciseState();
  };

  deleteExercise = () => {
    axios({
      method: "delete",
      url: "/services/rest/exercise?id=" + this.state.id
    }).then(res => {
      this.getExerciseData();
      this.clearExerciseState();
    });
  };

  validate = () => {
    return {
      commonName: this.state.commonName.length === 0,
      sets: this.state.sets.length === 0,
      durationPerSet: this.state.durationPerSet.length === 0
    };
  };

  canBeSaved = () => {
    const errors = this.validate();
    console.log(errors);
    const isDisabled = Object.keys(errors).some(x => errors[x]);
    return !isDisabled;
  };

  saveModal = () => {
    if (!this.canBeSaved()) {
      return;
    }

    if (this.state.id) {
      axios({
        method: "put",
        url: "/services/rest/exercise",
        data: {
          commonName: this.state.commonName,
          id: this.state.id,
          machineId: this.state.machineId ? this.state.machineId.value : null,
          sets: this.state.sets,
          branch: this.state.branch,
          durationPerSet: this.state.durationPerSet
        }
      }).then(res => {
        this.getExerciseData();
      });
    } else {
      axios({
        method: "put",
        url: "/services/rest/exercise",
        data: {
          commonName: this.state.commonName,
          machineId: this.state.machineId ? this.state.machineId.value : null,
          sets: this.state.sets,
          branch: this.state.branch,
          durationPerSet: this.state.durationPerSet
        }
      }).then(res => {
        this.getExerciseData();
      });
    }

    this.clearExerciseState();
  };

  getExerciseData() {
    axios
      .get("/services/rest/exercise?branch=" + this.state.branch)
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
    this.getExerciseData();
  }

  updateInputValue = evt => {
    this.setState({
      [evt.target.name]: evt.target.value
    });
  };

  handleChange = selectedOption => {
    this.setState({ machineId: selectedOption });
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
          handleDelete={this.deleteExercise}
          add={this.state.add}
          manipulated={this.state.isManager}
        >
          <h1>Exercise</h1>
          <div>
            <label>Common Name</label>
            <input
              className={errors.commonName ? "error" : ""}
              name="commonName"
              value={this.state.commonName}
              onChange={this.updateInputValue}
              readOnly={this.state.isManager}
            />
          </div>
          <div>
            <label>Machine Name</label>
            <div className="react-select-container">
              <Select
                value={this.state.machineId}
                onChange={this.handleChange}
                options={this.state.machineNames}
                isDisabled={this.state.isManager}
              />
            </div>
          </div>
          <div>
            <label>Sets</label>
            <input
              className={errors.sets ? "error" : ""}
              name="sets"
              value={this.state.sets}
              onChange={this.updateInputValue}
              readOnly={this.state.isManager}
            />
          </div>
          <div>
            <label>Duration Per Set (minutes)</label>
            <input
              className={errors.durationPerSet ? "error" : ""}
              name="durationPerSet"
              value={this.state.durationPerSet}
              onChange={this.updateInputValue}
              readOnly={this.state.isManager}
            />
          </div>
          <div>
            <label>ID</label>
            <input name="id" value={this.state.id} readOnly />
          </div>
        </Modal>
        <button
          className="add"
          type="button"
          onClick={this.showAddModal}
          disabled={this.state.isManager}
        >
          Add
        </button>
        <ReactTable
          data={this.state.data}
          columns={[
            {
              Header: "Common Name",
              accessor: "commonName"
            },
            {
              Header: "Machine ID",
              accessor: "machineId"
            },
            {
              Header: "Sets",
              accessor: "sets"
            },
            {
              Header: "Duration Per Set",
              accessor: "durationPerSet"
            },
            {
              Header: "ID",
              accessor: "id"
            }
          ]}
          defaultPageSize={50}
          showPaginationBottom={false}
          className="-striped -highlight"
          getTrProps={(state, rowInfo) => {
            if (rowInfo && rowInfo.row) {
              var selectedOption;
              for (var i = 0; i < this.state.machineNames.length; i++) {
                if (
                  this.state.machineNames[i].value === rowInfo.row.machineId
                ) {
                  selectedOption = this.state.machineNames[i];
                }
              }

              return {
                onClick: e => {
                  this.setState({
                    commonName: rowInfo.row.commonName,
                    sets: rowInfo.row.sets,
                    durationPerSet: rowInfo.row.durationPerSet,
                    machineId: selectedOption,
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

export default Exercise;
