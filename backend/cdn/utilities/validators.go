package utilities

import (
	"errors"
	"regexp"
)

func ValidatePort(port int) error {
	if port < 1 || port > 65535 {
		return errors.New("you entered invalid port number")
	}
	return nil
}

func ValidateFilenamePngOrJpg(filename string) error {
	regexValidationString := "^.*\\.(jpg|JPG|jpeg|JPEG|png|PNG)"
	if err := ValidateString(filename, regexValidationString); err != nil {
		return errors.New("you entered file with extension that is not one of supported")
	}
	return nil
}

func ValidateString(text, regex string) error {
	reg, err := regexp.Compile(regex)
	if err != nil {
		return errors.New("can't compile current regex string")
	}
	if !reg.MatchString(text) {
		return errors.New("the following text doesn't match the regular expression you entered")
	}
	return nil
}
