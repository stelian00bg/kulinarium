package services

import (
	"bytes"
	"io"
	"mime/multipart"
	"net/http"
	"net/http/httptest"
	"os"
	"path/filepath"
	"testing"

	"github.com/stretchr/testify/assert"
)

var (
	isi = ImageServiceImpl{}
)

func TestGetImageToken(t *testing.T) {
	IMAGE_TOKEN = ""
	err := os.Setenv("API_IMAGE_TOKEN", "token")
	assert.Nil(t, err, "shouldn't return error")
	res := getImageToken()
	assert.Equal(t, "token", res, "should be equal")
}

func TestGetSavedImagePNGFormat(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	file, err := os.Create(PATH_TO_IMAGES + "somefile.png")
	assert.Nil(t, err, "shouldnt return error while creating file")
	defer func() {
		file.Close()
		os.Remove(PATH_TO_IMAGES + "somefile.png")
	}()
	err = isi.GetSavedImage("somefile.png", httptest.NewRecorder())
	assert.NotNil(t, err, "should return error")
}

func TestGetSavedImageJPGFormat(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	file, err := os.Create(PATH_TO_IMAGES + "somefile.jpg")
	assert.Nil(t, err, "shouldnt return error while creating file")
	defer func() {
		file.Close()
		os.Remove(PATH_TO_IMAGES + "somefile.jpg")
	}()
	err = isi.GetSavedImage("somefile.jpg", httptest.NewRecorder())
	assert.NotNil(t, err, "should return error")
}

func TestGetSavedImageJPEGFormat(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	file, err := os.Create(PATH_TO_IMAGES + "somefile.jpeg")
	assert.Nil(t, err, "shouldnt return error while creating file")
	defer func() {
		file.Close()
		os.Remove(PATH_TO_IMAGES + "somefile.jpeg")
	}()
	err = isi.GetSavedImage("somefile.jpeg", httptest.NewRecorder())
	assert.NotNil(t, err, "should return error")
}
func TestGetSavedImageWrongFormat(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	err := isi.GetSavedImage("somefile.wrongformat", httptest.NewRecorder())
	assert.NotNil(t, err, "should return error")
}

func TestGetSavedImageNotExistingImage(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	err := isi.GetSavedImage("somenotexistingfile.png", httptest.NewRecorder())
	assert.NotNil(t, err, "should return error")
}

func TestSaveImageFromRequestPNG(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	file, err := os.Create("./testdata/test.png")
	assert.Nil(t, err, "shouldn't return error while creating file")
	defer func() {
		file.Close()
		os.Remove(PATH_TO_IMAGES + "test.png")
	}()
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)
	part, err := writer.CreateFormFile("file", filepath.Base("test.png"))
	assert.Nil(t, err, "shouldn't return error while creating the form file")
	_, err = io.Copy(part, file)
	writer.WriteField("token", "token")
	err = writer.Close()
	assert.Nil(t, err, "shouldn't return error while closing writer")
	req, err := http.NewRequest("POST", "", body)
	assert.Nil(t, err, "shouldn't return error while creating request")
	req.Header.Set("Content-Type", writer.FormDataContentType())
	link, err := isi.SaveImageFromRequest(req)
	assert.Nil(t, err, "shouldn't return error")
	err = os.Remove(PATH_TO_IMAGES + link)
	assert.Nil(t, err, "shouldn't return error if the file is saved succesfully")
}

func TestSaveImageFromRequestJPG(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	file, err := os.Create("./testdata/test.jpg")
	assert.Nil(t, err, "shouldn't return error while creating file")
	defer func() {
		file.Close()
		os.Remove(PATH_TO_IMAGES + "test.jpg")
	}()
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)
	part, err := writer.CreateFormFile("file", filepath.Base("test.png"))
	assert.Nil(t, err, "shouldn't return error while creating the form file")
	_, err = io.Copy(part, file)
	writer.WriteField("token", "token")
	err = writer.Close()
	assert.Nil(t, err, "shouldn't return error while closng writer")
	req, err := http.NewRequest("POST", "", body)
	assert.Nil(t, err, "shouldn't return error while creating request")
	req.Header.Set("Content-Type", writer.FormDataContentType())
	link, err := isi.SaveImageFromRequest(req)
	assert.Nil(t, err, "shouldn't return error")
	err = os.Remove(PATH_TO_IMAGES + link)
	assert.Nil(t, err, "shouldn't return error if the file is saved succesfully")
}

func TestSaveImageFromRequestWrongFormat(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	file, err := os.Create("./testdata/test.asdsd")
	assert.Nil(t, err, "shouldn't return error while creating file")
	defer func() {
		file.Close()
		os.Remove(PATH_TO_IMAGES + "test.asdsd")
	}()
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)
	part, err := writer.CreateFormFile("file", filepath.Base("test.asdsd"))
	assert.Nil(t, err, "shouldn't return error while creating the form file")
	_, err = io.Copy(part, file)
	writer.WriteField("token", "token")
	err = writer.Close()
	assert.Nil(t, err, "shouldn't return error while closng writer")
	req, err := http.NewRequest("POST", "", body)
	assert.Nil(t, err, "shouldn't return error while creating request")
	req.Header.Set("Content-Type", writer.FormDataContentType())
	_, err = isi.SaveImageFromRequest(req)
	assert.NotNil(t, err, "should return error")
}

func TestSaveImageWrongFilenameGenerationCommand(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	COMMAND_GENERATING_NAME = "edsadsadkasdaskdsadas"
	file, err := os.Create("./testdata/test.jpg")
	assert.Nil(t, err, "shouldn't return error while creating file")
	defer func() {
		file.Close()
		os.Remove(PATH_TO_IMAGES + "test.jpg")
	}()
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)
	part, err := writer.CreateFormFile("file", filepath.Base("test.png"))
	assert.Nil(t, err, "shouldn't return error while creating the form file")
	_, err = io.Copy(part, file)
	writer.WriteField("token", "token")
	err = writer.Close()
	assert.Nil(t, err, "shouldn't return error while closng writer")
	req, err := http.NewRequest("POST", "", body)
	assert.Nil(t, err, "shouldn't return error while creating request")
	req.Header.Set("Content-Type", writer.FormDataContentType())
	_, err = isi.SaveImageFromRequest(req)
	assert.NotNil(t, err, "should return error")
	COMMAND_GENERATING_NAME = "cat /dev/urandom | env LC_CTYPE=C tr -cd 'a-f0-9' | head -c 64"
}

func TestSaveImageWrongTempPath(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./fidsfjisdfisdfjisdfsd/"
	file, err := os.Create("./testdata/test.jpg")
	assert.Nil(t, err, "shouldn't return error while creating file")
	defer func() {
		file.Close()
		os.Remove("./testdata/test.jpg")
	}()
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)
	part, err := writer.CreateFormFile("file", filepath.Base("test.png"))
	assert.Nil(t, err, "shouldn't return error while creating the form file")
	_, err = io.Copy(part, file)
	writer.WriteField("token", "token")
	err = writer.Close()
	assert.Nil(t, err, "shouldn't return error while closng writer")
	req, err := http.NewRequest("POST", "", body)
	assert.Nil(t, err, "shouldn't return error while creating request")
	req.Header.Set("Content-Type", writer.FormDataContentType())
	_, err = isi.SaveImageFromRequest(req)
	assert.NotNil(t, err, "should return error")
}

func TestSaveImageFromRequestWrongToken(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)
	writer.WriteField("token", "tokensdsfsdfsfs")
	err := writer.Close()
	assert.Nil(t, err, "shouldn't return error while closing writer")
	req, err := http.NewRequest("POST", "", body)
	assert.Nil(t, err, "shouldn't return error while creating request")
	req.Header.Set("Content-Type", writer.FormDataContentType())
	_, err = isi.SaveImageFromRequest(req)
	assert.NotNil(t, err, "should return error")
}

func TestSaveImageFromRequestWrongFieldName(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	file, err := os.Create("./testdata/test.png")
	assert.Nil(t, err, "shouldn't return error while creating file")
	defer func() {
		file.Close()
		os.Remove(PATH_TO_IMAGES + "test.png")
	}()
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)
	part, err := writer.CreateFormFile("fileNOTEXISTING", filepath.Base("test.png"))
	assert.Nil(t, err, "shouldn't return error while creating the form file")
	_, err = io.Copy(part, file)
	writer.WriteField("token", "token")
	err = writer.Close()
	assert.Nil(t, err, "shouldn't return error while closng writer")
	req, err := http.NewRequest("POST", "", body)
	assert.Nil(t, err, "shouldn't return error while creating request")
	req.Header.Set("Content-Type", writer.FormDataContentType())
	_, err = isi.SaveImageFromRequest(req)
	assert.NotNil(t, err, "should return error")
}

func TestDeleteImage(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	file, err := os.Create("./testdata/file.file")
	assert.Nil(t, err, "shouldn't return error while creating file")
	err = file.Close()
	assert.Nil(t, err, "shouldn't return error while closing file")
	err = isi.DeleteImage("token", "file.file")
	assert.Nil(t, err, "shouldn't return error")
}

func TestDeleteImageNotExistingImage(t *testing.T) {
	IMAGE_TOKEN = "token"
	PATH_TO_IMAGES = "./testdata/"
	err := isi.DeleteImage("token", "file.file")
	assert.NotNil(t, err, "should return error")
}

func TestDeleteImageWrongImage(t *testing.T) {
	IMAGE_TOKEN = "some image token"
	err := isi.DeleteImage("some other image token", "some place holder")
	assert.NotNil(t, err, "should return error")
}
