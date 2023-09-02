package cmd

import (
	"cdn/server"
	"errors"
	"log"
	"os"
	"strconv"
	"sync"
)

type Config struct {
	port int
}

func Start() {
	config := Config{}
	err := config.extractEnvVariables()
	if err != nil {
		log.Println(err)
		return
	}

	var wg sync.WaitGroup
	wg.Add(1)
	go func() {
		defer wg.Done()
		err := server.Start(config.port)
		if err != nil {
			log.Fatalln(err)
		}
	}()

	log.Printf("Server started listening on port %d", config.port)
	wg.Wait()
}

func (c *Config) extractEnvVariables() error {
	portString := os.Getenv("API_PORT")
	if portString == "" {
		return errors.New("can't get API_PORT environment variable")
	}
	port, err := strconv.Atoi(portString)
	if err != nil {
		return errors.New("can't parse port number")
	}
	c.port = port
	return nil
}
