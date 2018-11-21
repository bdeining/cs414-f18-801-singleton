import React, { Component } from "react";
import axios from "axios";

class LoginPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      email: "",
      password: ""
    };
    localStorage.removeItem("user");
  }

  handleSubmit = e => {
    e.preventDefault();
    axios
      .get(
        "/services/rest/login?email=" +
          this.state.email +
          "&password=" +
          this.state.password
      )
      .then(res => {
        localStorage.setItem("user", this.state.email);
        this.setState({
          email: this.state.email,
          password: this.state.password
        });
      });
  };

  updateInputValue = evt => {
    this.setState({
      [evt.target.name]: evt.target.value
    });
  };

  render() {
    const isLoggedIn = localStorage.getItem("user");
    console.log(isLoggedIn);
    return (
      <div>
        {!isLoggedIn ? (
          <div>
            <h2>Login</h2>
            <form name="form" onSubmit={this.handleSubmit}>
              <div>
                <label>Email</label>
                <input
                  name="email"
                  value={this.state.email}
                  onChange={this.updateInputValue}
                />
                <label>Password</label>
                <input
                  name="password"
                  value={this.state.password}
                  onChange={this.updateInputValue}
                />
                <input type="submit" value="Submit" />
              </div>
            </form>
          </div>
        ) : (
          <div>You are logged in. Use the tabs to navigate.</div>
        )}
      </div>
    );
  }
}

export default LoginPage;
