package utilities

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestGetLocalAdress(t *testing.T) {
	adress, err := GetLocalAdress(8080)
	assert.Nil(t, err, "shouldn't return error")
	assert.Equal(t, ":8080", adress, "should be the same")
}

func TestGetLocalAdressNotValidPort(t *testing.T) {
	_, err := GetLocalAdress(8238488242)
	assert.NotNil(t, err, "should returne error")
}
