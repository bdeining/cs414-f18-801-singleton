import React, { Component } from "react";
import axios from "axios";
import ReactTable from "react-table";
import "react-table/react-table.css";
import Select from "react-select";
import "./styles.css";

class Exercise extends React.Component {
  constructor() {
    super();
    this.state = {
      data: [],
      machineNames: [],
      show: false,
      id: "",
      selected: null
    };
    axios({
      method: "get",
      url: "/services/rest/machinenames"
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
      url: "/services/rest/machinenames"
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

  saveModal = () => {
    if (this.state.id) {
      axios({
        method: "put",
        url: "/services/rest/exercise",
        data: {
          commonName: this.state.commonName,
          id: this.state.id,
          machineId: this.state.machineId ? this.state.machineId.value : null,
          sets: this.state.sets,
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
          durationPerSet: this.state.durationPerSet
        }
      }).then(res => {
        this.getExerciseData();
      });
    }

    this.clearExerciseState();
  };

  getExerciseData() {
    axios.get("/services/rest/exercise").then(res => {
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
    return (
      <div>
        <Modal
          show={this.state.show}
          handleClose={this.hideModal}
          handleSave={this.saveModal}
          handleDelete={this.deleteExercise}
          add={this.state.add}
        >
          <h1>Exercise</h1>
          <div>
            <label>Common Name</label>
            <input
              name="commonName"
              value={this.state.commonName}
              onChange={this.updateInputValue}
            />
          </div>
          <div>
            <label>Machine Name</label>
            <div className="react-select-container">
              <Select
                value={this.state.machineId}
                onChange={this.handleChange}
                options={this.state.machineNames}
              />
            </div>
          </div>
          <div>
            <label>Sets</label>
            <input
              name="sets"
              value={this.state.sets}
              onChange={this.updateInputValue}
            />
          </div>
          <div>
            <label>Duration Per Set (minutes)</label>
            <input
              name="durationPerSet"
              value={this.state.durationPerSet}
              onChange={this.updateInputValue}
            />
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
          defaultPageSize={10}
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

export default Exercise;
