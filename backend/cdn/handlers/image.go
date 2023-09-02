package handlers

import (
	"cdn/services"
	"encoding/json"
	"net/http"

	"github.com/gorilla/mux"
)

type ImageHandler struct {
	services.ImageService
}

func (ih *ImageHandler) GetImageHandler(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	filename := vars["filename"]
	err := ih.GetSavedImage(filename, w)
	if err != nil {
		w.WriteHeader(http.StatusNotFound)
		w.Write([]byte(err.Error()))
	}
}

func (ih *ImageHandler) PostImageHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	filename, err := ih.SaveImageFromRequest(r)
	response := map[string]interface{}{}
	defer json.NewEncoder(w).Encode(response)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		response["error"] = err.Error()
		return
	}
	response["serverFilename"] = filename
	w.WriteHeader(http.StatusOK)
}

func (ih *ImageHandler) DeleteImageHandler(w http.ResponseWriter, r *http.Request) {
	filename := mux.Vars(r)["filename"]
	token := r.FormValue("token")
	err := ih.DeleteImage(token, filename)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte(err.Error()))
		return
	}
	w.WriteHeader(http.StatusNoContent)
}
