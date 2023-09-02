package handlers

import (
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestGetHealthHandler(t *testing.T) {
	req, err := http.NewRequest(http.MethodGet, "/health", nil)
	assert.Nil(t, err, "shouldn't return error")

	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(GetHealthHandler)

	handler.ServeHTTP(rr, req)
	assert.Equal(t, http.StatusNoContent, rr.Code, "should return code 204 - No Content")
}
