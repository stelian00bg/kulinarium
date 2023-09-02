package handlers

import (
	"cdn/services/mocks"
	"errors"
	"net/http"
	"net/http/httptest"
	"net/url"
	"testing"

	"github.com/gorilla/mux"
	"github.com/stretchr/testify/assert"
)

func TestGetImageHandler(t *testing.T) {
	mockImageService := mocks.ImageService{}
	ih := ImageHandler{ImageService: &mockImageService}
	req, err := http.NewRequest(http.MethodGet, "/image", nil)
	assert.Nil(t, err, "shouldn't return error")
	req = mux.SetURLVars(req, map[string]string{
		"filename": "somefile.png",
	})

	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(ih.GetImageHandler)

	mockImageService.On("GetSavedImage", "somefile.png", rr).
		Return(nil)
	ih.ImageService = &mockImageService

	handler.ServeHTTP(rr, req)
	assert.Equal(t, http.StatusOK, rr.Code, "should return code 200 - OK")
	mockImageService.AssertCalled(t, "GetSavedImage", "somefile.png", rr)
	mockImageService.AssertExpectations(t)
}

func TestGetImageHandlerError(t *testing.T) {
	mockImageService := mocks.ImageService{}
	ih := ImageHandler{ImageService: &mockImageService}

	req, err := http.NewRequest(http.MethodGet, "/image", nil)
	assert.Nil(t, err, "shouldn't return error")
	req = mux.SetURLVars(req, map[string]string{
		"filename": "somefile.png",
	})

	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(ih.GetImageHandler)

	mockImageService.On("GetSavedImage", "somefile.png", rr).
		Return(errors.New("some error returned here"))
	ih.ImageService = &mockImageService

	handler.ServeHTTP(rr, req)
	assert.Equal(t, http.StatusNotFound, rr.Code, "should return code 404 - Not Found")
	mockImageService.AssertCalled(t, "GetSavedImage", "somefile.png", rr)
	mockImageService.AssertExpectations(t)
}

func TestPostImageHandler(t *testing.T) {
	mockImageService := mocks.ImageService{}
	ih := ImageHandler{ImageService: &mockImageService}

	req, err := http.NewRequest(http.MethodPost, "/image", nil)
	assert.Nil(t, err, "shouldn't return error")

	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(ih.PostImageHandler)

	mockImageService.On("SaveImageFromRequest", req).
		Return("something", nil)
	ih.ImageService = &mockImageService
	handler.ServeHTTP(rr, req)
	assert.Equal(t, http.StatusOK, rr.Code, "should return code 200 - Status OK")
	mockImageService.AssertCalled(t, "SaveImageFromRequest", req)
	mockImageService.AssertExpectations(t)
}

func TestPostImageHandlerError(t *testing.T) {
	mockImageService := mocks.ImageService{}
	ih := ImageHandler{ImageService: &mockImageService}

	req, err := http.NewRequest(http.MethodPost, "/image", nil)
	assert.Nil(t, err, "shouldn't return error")

	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(ih.PostImageHandler)

	mockImageService.On("SaveImageFromRequest", req).
		Return("something", errors.New("some error returned here"))
	ih.ImageService = &mockImageService

	handler.ServeHTTP(rr, req)
	assert.Equal(t, http.StatusInternalServerError, rr.Code, "should return code 500 - Internal Server Error")
	mockImageService.AssertCalled(t, "SaveImageFromRequest", req)
	mockImageService.AssertExpectations(t)
}

func TestDeleteImageHandler(t *testing.T) {
	mockImageService := mocks.ImageService{}
	ih := ImageHandler{ImageService: &mockImageService}

	req, err := http.NewRequest(http.MethodDelete, "/image", nil)
	assert.Nil(t, err, "shouldn't return error")
	req = mux.SetURLVars(req, map[string]string{
		"filename": "file",
	})
	form := url.Values{}
	form.Add("token", "token")
	req.PostForm = form

	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(ih.DeleteImageHandler)

	mockImageService.On("DeleteImage", "token", "file").
		Return(nil)
	ih.ImageService = &mockImageService

	handler.ServeHTTP(rr, req)
	assert.Equal(t, http.StatusNoContent, rr.Code, "should return code 204 - No Content")
	mockImageService.AssertCalled(t, "DeleteImage", "token", "file")
	mockImageService.AssertExpectations(t)
}

func TestDeleteImageHandlerError(t *testing.T) {
	mockImageService := mocks.ImageService{}
	ih := ImageHandler{ImageService: &mockImageService}

	req, err := http.NewRequest(http.MethodDelete, "/image", nil)
	assert.Nil(t, err, "shouldn't return error")
	req = mux.SetURLVars(req, map[string]string{
		"filename": "file",
	})
	form := url.Values{}
	form.Add("token", "token")
	req.PostForm = form

	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(ih.DeleteImageHandler)

	mockImageService.On("DeleteImage", "token", "file").
		Return(errors.New("some error returned here"))
	ih.ImageService = &mockImageService

	handler.ServeHTTP(rr, req)
	assert.Equal(t, http.StatusInternalServerError, rr.Code, "should return code 500 - Internal Server Error")
	mockImageService.AssertCalled(t, "DeleteImage", "token", "file")
	mockImageService.AssertExpectations(t)
}
