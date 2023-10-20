import React, { useState, useCallback } from 'react';
import { Button, Card, Form, InputGroup } from 'react-bootstrap';

type InputItem = {
    value: string;
}

type KeywordInputsProps = {
    onValuesChange: (values: string[]) => void;
}

/**
 * キーワード入力欄を表示するコンポーネント
 * @param onValuesChange キーワード入力欄の値が変更されたときに呼び出される関数
 */
const KeywordInputs: React.FC<KeywordInputsProps> = ({ onValuesChange }) => {

    const [inputList, setInputList] = useState<InputItem[]>([{ value: '' }]);

    const notifyValuesChange = useCallback(() => {
        const values = inputList.filter(item => item.value !== '').map(item => item.value);
        onValuesChange(values);
    }, [inputList, onValuesChange]);

    const handleInputChange = (value: string, index: number) => {
        const list = [...inputList];
        list[index].value = value;
        setInputList(list);
        //        console.log(list);
        notifyValuesChange();
    };

    const handleAddInput = () => {
        setInputList([...inputList, { value: '' }]);
    };

    const handleRemoveInput = (index: number) => {
        const list = [...inputList];
        list.splice(index, 1);
        setInputList(list);
    };

    return (
        <Card>
            <Card.Header>文章生成時に追加するキーワード</Card.Header>
            <Card.Body>
                {inputList.map((input, index) => (
                    <InputGroup key={index}>
                        <InputGroup.Text>Keyword</InputGroup.Text>
                        <Form.Control
                            type="text"
                            name={`keywordInput-${index}`}
                            value={input.value}
                            onChange={e => handleInputChange(e.target.value, index)}
                        />
                        {inputList.length !== 1 && (
                            <Button onClick={() => handleRemoveInput(index)}>-</Button>
                        )}
                    </InputGroup>
                ))}
                <Button variant="secondary" onClick={handleAddInput}>+</Button>
            </Card.Body>
        </Card>
    );
}

export default KeywordInputs;