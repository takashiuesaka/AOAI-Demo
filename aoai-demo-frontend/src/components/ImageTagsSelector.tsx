import React from 'react'
import { Card, Form } from 'react-bootstrap'

type Props = {
    analyzedImageTags: string[],
    handleImageTagsCheckboxChange: (isChecked: boolean, item: string) => void
}
/**
 * 画像から抽出したタグを表示して選択するコンポーネント
 * @param analyzedImageTags 画像から抽出したタグ
 * @param handleImageTagsCheckboxChange チェックボックスのチェックイベント
 */
const ImageTagsSelector = ({ analyzedImageTags, handleImageTagsCheckboxChange }: Props) => {
    return (
        <Card>
            <Card.Header>画像から得た情報</Card.Header>
            <Card.Body>
                <Form id='analyzedImageTagsForm'>
                    {analyzedImageTags.map((tag, index) => (
                        <Form.Check
                            key={`image-checkbox-${index}`}
                            id={`image-checkbox-${index}`}
                            type='checkbox'
                            inline
                            onChange={event => handleImageTagsCheckboxChange(event.target.checked, tag)}
                            label={tag}
                        />
                    ))}
                </Form>

            </Card.Body>
        </Card>
    )
}

export default ImageTagsSelector