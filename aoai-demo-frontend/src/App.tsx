import React, { useState } from 'react';
import axios from 'axios'
import { useAsyncCallback } from 'react-async-hook'
import { Button, Card, Col, Container, Navbar, Row } from 'react-bootstrap'
import './App.css'
import ImageAnalyzer from './components/ImageAnalyzer';
import ImageTagsSelector from './components/ImageTagsSelector';
import KeywordInputs from './components/KeywordsInputs';

const App = () => {
  /*** state area ***/
  // 画像タグの選択
  type inputImageTagType = {
    imageTag: string;
    isChecked: boolean;
  }

  // AOAIに投げる画像タグとキーワードの入力値を保持する型
  type keywordsType = {
    imageTags: inputImageTagType[];
    inputKeywords: string[];
  }

  // 画像タグの選択、入力キーワードの入力値
  const [keywords, setKeywords] = useState<keywordsType>({ imageTags: [], inputKeywords: [] })

  // 文章生成結果
  const [generatedText, setGeneratedText] = useState<string>('')
  /*** state area ***/

  /**
   * 画像から得たタグを受け取る
   */
  const acceptImageTags = (tags: string[]) => {
    // いずれ複数の画像から得たタグをマージする処理をここに追加する
    setKeywords(prevKeywords => {
      const copiedKeywords = { ...prevKeywords };
      copiedKeywords.imageTags = tags.map<inputImageTagType>((tag) => { return { imageTag: tag, isChecked: false } })
      return copiedKeywords
    })
  }

  /**
  * 画像から得た情報（タグ）のチェックボックスのチェックイベントのハンドラ
  */
  const handleImageTagsCheckboxChange = (isChecked: boolean, item: string) => {

    setKeywords(prevKeywords => {
      const copiedKeywords = { ...prevKeywords };

      copiedKeywords.imageTags.find((selectedItem) => selectedItem.imageTag === item)!.isChecked = isChecked;

      return copiedKeywords;
    })

  };

  /**
   * 手動入力のキーワードの入力イベントハンドラ
   */
  const handleInputKeywordsValuesChange = (values: string[]) => {
    setKeywords(prevKeywords => {
      const copiedKeywords = { ...prevKeywords };
      copiedKeywords.inputKeywords = values;
      return copiedKeywords;
    })
  };

  /**
   * 文章生成API呼び出し
   */
  const generateText = async (infos: string[]) => {
    try {
      const response = await axios.post('api/messages', { keywords: infos });
      setGeneratedText(response.data.tags[0])
    } catch (e) {
      console.log(e)
    }
  }

  /**
  * 文章生成Async Wrapper
  */
  const asyncGenerateTextEvent = useAsyncCallback(async (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {

    const args = [...keywords.imageTags.filter(item => item.isChecked).map(item => item.imageTag), ...keywords.inputKeywords]
    // 生成済みの文章をクリア
    setGeneratedText('')
    await generateText(args)
  })

  return (
    <>
      <Navbar expand="lg" className="bg-body-tertiary" data-bs-theme="dark" >
        <Container>
          <Navbar.Brand href="#home">Azure AOAI Demo Application</Navbar.Brand>
        </Container>
      </Navbar>
      <main>
        <Container>
          <Row>
            <Col>
              <ImageAnalyzer acceptImageTags={acceptImageTags} />
            </Col>
          </Row>
          <Row>
            <Col>
              <Card>
                <Card.Body>
                  <Card.Title>お勧め文章の生成</Card.Title>
                  <ImageTagsSelector analyzedImageTags={keywords.imageTags.map(tags => tags.imageTag)} handleImageTagsCheckboxChange={handleImageTagsCheckboxChange} />
                  <KeywordInputs onValuesChange={handleInputKeywordsValuesChange} />
                  <Button variant='primary' style={{ marginTop: 10 }} onClick={asyncGenerateTextEvent.execute} disabled={keywords.imageTags.length + keywords.inputKeywords.length === 0}>
                    {asyncGenerateTextEvent.loading ? "生成中・・・" : "文章生成"}
                  </Button>
                  <div>{generatedText}</div>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </main >
      <footer className='footerBottom'>
        <Row>
          <Col>
            2023 Microsoft Japan SMC/DS Azure Team
          </Col>
        </Row>
      </footer>

    </>
  )
}

export default App;
