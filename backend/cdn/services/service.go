package services

import "net/http"

type ImageService interface {
	GetSavedImage(string, http.ResponseWriter) error
	SaveImageFromRequest(*http.Request) (string, error)
	DeleteImage(string, string) error
}
