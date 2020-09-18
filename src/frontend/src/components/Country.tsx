import {ICountry} from "../interface/ICountry";
import stores from "../store";
import SelectObject from "./SelectObject";
import * as React from "react";
import {Form} from "antd";
import {observer} from "mobx-react";
import {FormComponentProps} from "antd/es/form";
import {observable} from "mobx";

interface IProps extends FormComponentProps {
}

@observer
class Country extends React.Component<IProps> {

    componentDidMount() {
        stores.countryStore.loadPage();
    }

    @observable country: ICountry = {} as ICountry;

    render() {

        const { form } = this.props;
        const { getFieldDecorator } = form;

        return <Form>
            <Form.Item label="Страна: ">
                {getFieldDecorator('country', {
                    initialValue: this.country,
                })(
                    <SelectObject<ICountry>
                        allowClear={true}
                        dataSource={stores.countryStore.countryPage.list}
                        onChange={e => this.country = e}
                        onScroll={() => stores.countryStore.nextPage()}
                        onSearch={(value => stores.countryStore.loadPage(value))}
                    />
                )}
            </Form.Item>
        </Form>
    }
}
export default Form.create<IProps>()(Country);
