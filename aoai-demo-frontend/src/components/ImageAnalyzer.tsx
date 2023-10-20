import React, { useState, useRef } from 'react';
import axios from 'axios'
import { Button, Card } from "react-bootstrap"
import { useAsyncCallback } from 'react-async-hook'

type Props = {
    acceptImageTags: (tags: Array<string>) => void
}

/**
 * 画像をアップロードして、画像から抽出したタグを表示するコンポーネント
 * @param acceptImageTags 画像から抽出したタグを親コンポーネントに渡す関数
 */
const ImageAnalyzer = ({ acceptImageTags }: Props) => {

    const inputRef = useRef<HTMLInputElement>(null)
    const [imageBinaryData, setImageBinaryData] = useState<string | ArrayBuffer | null>(null);

    const uploadFile = async (file: Blob) => {
        if (!file) return

        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await axios.post('api/images', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });

            acceptImageTags(response.data.tags)
            // console.log(response.data.tags)

        } catch (e) {
            console.log(e)
        }
    }

    const showLocalImage = (file: Blob) => {
        const reader = new FileReader();
        reader.onload = (e: ProgressEvent<FileReader>) => setImageBinaryData(e.target?.result ?? null);
        reader.readAsDataURL(file);
    }

    const clickFileUploadButton = () => {
        inputRef.current?.click()
    }

    const asyncImageUploadEvent = useAsyncCallback(async (event) => {
        const file = event.target.files[0]
        if (file) {
            setImageBinaryData(null)
            await uploadFile(file)
            showLocalImage(file)

        }
    });

    return (
        <Card style={{ height: 500 }}>
            <Card.Header style={{ textAlign: 'center' }}>
                <Button variant='primary' onClick={clickFileUploadButton} disabled={asyncImageUploadEvent.loading}>
                    {asyncImageUploadEvent.loading ? 'アップロード中...' : '画像を選択'}
                </Button>
            </Card.Header>
            <Card.Body>
                <div className='image-container'>
                    {imageBinaryData && <img src={imageBinaryData.toString()} alt="Selected" />}
                </div>
            </Card.Body>
            <input hidden type='file' ref={inputRef} accept='image/*,.png,.jpg,.jpeg,.gif' onChange={asyncImageUploadEvent.execute} />
            <div>
                {asyncImageUploadEvent.loading ? <div>アップロード中...</div> : null}
                {asyncImageUploadEvent.error ? <div>エラー: {asyncImageUploadEvent.error.message}</div> : null}
                {asyncImageUploadEvent.result ? <div>アップロード完了: {JSON.stringify(asyncImageUploadEvent.result)}</div> : null}
            </div>
        </Card>
    )
}

export default ImageAnalyzer