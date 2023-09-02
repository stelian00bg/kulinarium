package server

import (
	"cdn/handlers"
	"cdn/services"
	"cdn/utilities"
	"log"
	"net/http"

	"github.com/gorilla/mux"
)

type Server struct {
	router *mux.Router
}

func Start(port int) error {
	server := Server{router: mux.NewRouter()}
	server.initialize()

	adress, err := utilities.GetLocalAdress(port)
	if err != nil {
		return err
	}
	log.Fatal(http.ListenAndServe(adress, server.router))
	return nil
}

func (s *Server) initialize() {
	s.loadRouters()
}

func (s *Server) loadRouters() {
	s.loadImageRoutes()
	s.loadHealthRoutes()
}

func (s *Server) loadImageRoutes() {
	ig := handlers.ImageHandler{
		ImageService: &services.ImageServiceImpl{},
	}
	s.Get("/image/{filename}", ig.GetImageHandler)
	s.Post("/image", ig.PostImageHandler)
	s.Delete("/image/{filename}", ig.DeleteImageHandler)
}

func (s *Server) loadHealthRoutes() {
	s.Get("/health", handlers.GetHealthHandler)
}

func (s *Server) Get(path string, f func(http.ResponseWriter, *http.Request)) {
	s.router.HandleFunc(path, f).Methods(http.MethodGet)
}

func (s *Server) Post(path string, f func(http.ResponseWriter, *http.Request)) {
	s.router.HandleFunc(path, f).Methods(http.MethodPost)
}

func (s *Server) Delete(path string, f func(http.ResponseWriter, *http.Request)) {
	s.router.HandleFunc(path, f).Methods(http.MethodDelete)
}
