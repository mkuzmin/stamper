import React, {Component} from "react";
import {Link} from "react-router-dom";
import {StatusIcon} from "./common";

class Template extends Component {
    static runStack() {
        alert("hello!");
    }

    render() {
        return (
            <li className="list-group-item">
                <span>{this.props.template.name}</span>
                <Link to={`/template/${this.props.template.name}/run`}
                      className="btn btn-xs btn-success pull-right">Run</Link>
            </li>
        );
    }
}

class TemplateList extends Component {
    render() {
        const rows = [];
        this.props.templates.forEach(template => {
            rows.push(<Template template={template} key={template.name}/>);
        });
        return (
            <div>
                <h2>Available Templates:</h2>
                <ul className="list-group">
                    {rows}
                </ul>
            </div>);
    }
}

class StackList extends Component {
    render() {
        const rows = [];
        this.props.stacks.forEach(function (stack) {
            rows.push(<Stack stack={stack} key={stack.name}/>);
        });
        return (
            <div>
                <h2>Running Stacks:</h2>
                <ul className="list-group">
                    {rows}
                </ul>
            </div>);
    }
}

class Stack extends Component {
    static destroy() {
        alert("destroy!");
    }

    render() {
        return (
            <li className="list-group-item">
                <StatusIcon status={this.props.stack.status}/> {" "}
                <Link to={`/stack/${this.props.stack.name}`}>{this.props.stack.name}</Link>
                <button className="btn btn-xs btn-danger pull-right" onClick={Stack.destroy}>Destroy</button>
            </li>
        );
    }
}

export class MainPage extends Component {
    constructor(props) {
        super(props);
        this.state = {templates: [], stacks: []};
    }

    loadTemplatesFromServer() {
        fetch("/api/templates", {method: "get", credentials: "same-origin"})
            .then(result => result.json())
            .then(data => this.setState({templates: data}));
    }

    loadStacksFromServer() {
        fetch("/api/stacks", {method: "get", credentials: "same-origin"})
            .then(result => result.json())
            .then(data => this.setState({stacks: data}));
    }

    componentDidMount() {
        this.loadTemplatesFromServer();
        this.loadStacksFromServer();
        setInterval(() => {
            this.loadStacksFromServer();
        }, 2000);
    }

    render() {
        return (
            <div>
                <TemplateList templates={this.state.templates}/>
                <br/>
                <StackList stacks={this.state.stacks}/>
            </div>
        );
    }
}