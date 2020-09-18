import * as React from 'react';
import '@scss/main.scss';
import '@css/antd.css';
import {observer} from "mobx-react";
import Country from "./components/Country";

interface IProps {
}

@observer
class App extends React.Component<IProps> {

    constructor(props: IProps) {
        super(props);
    }

    render() {
        return <React.Fragment>
            <Country/>
        </React.Fragment>
    }
}

export default App;
