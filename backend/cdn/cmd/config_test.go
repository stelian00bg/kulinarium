package cmd

import (
	"os"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestStartNotExistingPort(t *testing.T) {
	err := os.Setenv("API_PORT", "")
	assert.Nil(t, err, "shouldn't return error")
	Start()
}

func TestExtractEnvVariables(t *testing.T) {
	os.Setenv("API_PORT", "232")
	c := Config{}
	err := c.extractEnvVariables()
	assert.Nil(t, err, "shouldn't return error")
	assert.Equal(t, 232, c.port, "should be equal")
}

func TestExtractEnvVariablesEmptyVariable(t *testing.T) {
	os.Setenv("API_PORT", "")
	c := Config{}
	err := c.extractEnvVariables()
	assert.NotNil(t, err, "should return error")
}

func TestExtractEnvVariablesNotValidVariable(t *testing.T) {
	os.Setenv("API_PORT", "234dsd")
	c := Config{}
	err := c.extractEnvVariables()
	assert.NotNil(t, err, "should return error")
}
