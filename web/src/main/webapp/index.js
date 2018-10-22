import React from 'react';
import { render } from 'react-dom';
import { Switch, Route, BrowserRouter as Router, NavLink } from 'react-router-dom';
import Bootstrap from 'bootstrap3/dist/css/bootstrap.css';
import { Button } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { applyMiddleware, createStore, compose } from 'redux';
import createSagaMiddleware from 'redux-saga';
import { connect, Provider } from 'react-redux';
import { counterReducer } from './reducers';
import Home from './components/Home';
import About from './components/About';
import Counter from './components/Counter';
import { rootSaga } from './sagas';

const sagaMiddleware = createSagaMiddleware();
const store = createStore(counterReducer, compose(applyMiddleware(sagaMiddleware), window.devToolsExtension ? window.devToolsExtension() : f => f));
sagaMiddleware.run(rootSaga);

render(
    <Provider store={store}>
        <Router>
            <div className="App">
                <div className="container">
                    <LinkContainer to="/frontend-karaf-demo/"><Button>Customers</Button></LinkContainer>
                    <LinkContainer to="/frontend-karaf-demo/counter"><Button>Trainers</Button></LinkContainer>
                    <LinkContainer to="/frontend-karaf-demo/about"><Button>Workout Routine</Button></LinkContainer>
                    <LinkContainer to="/frontend-karaf-demo/about"><Button>Exercises</Button></LinkContainer>
                    <LinkContainer to="/frontend-karaf-demo/about"><Button>Machines</Button></LinkContainer>
                    <hr/>
                </div>
                <Switch>
                    <Route exact path="/frontend-karaf-demo/" component={Home} />
                    <Route path="/frontend-karaf-demo/counter" component={Counter} />
                    <Route path="/frontend-karaf-demo/about" component={About} />
                </Switch>
            </div>
        </Router>
    </Provider>,
    document.getElementById('root')
);
