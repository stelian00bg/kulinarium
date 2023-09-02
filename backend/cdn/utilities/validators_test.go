package utilities

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestValidatePort(t *testing.T) {
	err := ValidatePort(423)
	assert.Nil(t, err, "shouldn't return error")
}

func TestValidatePortOutOfRangeNegative(t *testing.T) {
	err := ValidatePort(-423)
	assert.NotNil(t, err, "should return error")
}

func TestValidatePortOutOfRangePossitive(t *testing.T) {
	err := ValidatePort(423432423)
	assert.NotNil(t, err, "should return error")
}

func TestValidateFilenamePngOrJpgPNG(t *testing.T) {
	err := ValidateFilenamePngOrJpg("somefile.png")
	assert.Nil(t, err, "shouldnt return error")
	err = ValidateFilenamePngOrJpg("somefile.PNG")
	assert.Nil(t, err, "shouldnt return error")
}

func TestValidateFilenamePngOrJpgJPEG(t *testing.T) {
	err := ValidateFilenamePngOrJpg("somefile.jpg")
	assert.Nil(t, err, "shouldnt return error")
	err = ValidateFilenamePngOrJpg("somefile.JPG")
	assert.Nil(t, err, "shouldnt return error")
	err = ValidateFilenamePngOrJpg("somefile.jpeg")
	assert.Nil(t, err, "shouldnt return error")
	err = ValidateFilenamePngOrJpg("somefile.JPEG")
	assert.Nil(t, err, "shouldnt return error")
}

func TestValidateFilenamePngOrJpgNotValid(t *testing.T) {
	err := ValidateFilenamePngOrJpg("somefile.somethignnotvalid")
	assert.NotNil(t, err, "should return error")
}

func TestValidateStringMatchingString(t *testing.T) {
	err := ValidateString("sss.png", "^.*\\.(jpg|JPG|jpeg|JPEG|png|PNG)")
	assert.Nil(t, err, "shouldn't return error")
}

func TestValidateStringNotMatchingString(t *testing.T) {
	err := ValidateString("sss", "^.*\\.(jpg|JPG|jpeg|JPEG|png|PNG)")
	assert.NotNil(t, err, "should return error")
}

func TestValidateStringUncompileableRegex(t *testing.T) {
	err := ValidateString("sss", `[ ]\K(?<!\d )(?=(?: ?\d){8})(?!(?: ?\d){9})\d[ \d]+\d`)
	assert.NotNil(t, err, "should return error")
}
