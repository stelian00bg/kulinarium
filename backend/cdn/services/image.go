package services

import (
	"cdn/utilities"
	"errors"
	"fmt"
	"image/jpeg"
	"image/png"
	"io"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"os/exec"
	"path/filepath"
)

var (
	IMAGE_TOKEN             = ""
	PATH_TO_IMAGES          = "./tmp/images/"
	COMMAND_GENERATING_NAME = "cat /dev/urandom | env LC_CTYPE=C tr -cd 'a-f0-9' | head -c 64"
)

func getImageToken() string {
	if IMAGE_TOKEN == "" {
		IMAGE_TOKEN = os.Getenv("API_IMAGE_TOKEN")
	}
	return IMAGE_TOKEN
}

type ImageServiceImpl struct{}

func (isi *ImageServiceImpl) GetSavedImage(filename string, writer http.ResponseWriter) error {
	err := utilities.ValidateFilenamePngOrJpg(filename)
	if err != nil {
		log.Println(err)
		return errors.New("you entered wrong file format")
	}
	file, err := os.Open(PATH_TO_IMAGES + filename)
	if err != nil {
		log.Println(err)
		return errors.New("can't find the following image in the server space")
	}
	defer file.Close()
	ext := filepath.Ext(file.Name())
	switch ext {
	case ".png", ".PNG":
		img, err := png.Decode(file)
		if err != nil {
			log.Println(err)
			return errors.New("couldn't decode image")
		}
		if err := png.Encode(writer, img); err != nil {
			log.Println(err)
			return errors.New("couldn't encode image")
		}
	case ".jpeg", ".jpg", ".JPEG", ".JPG":
		img, err := jpeg.Decode(file)
		if err != nil {
			log.Println(err)
			return errors.New("couldn't decode image")
		}
		if err := jpeg.Encode(writer, img, nil); err != nil {
			log.Println(err)
			return errors.New("couldn't encode image")
		}
	}
	return nil
}

func (isi *ImageServiceImpl) SaveImageFromRequest(r *http.Request) (string, error) {
	current := r.FormValue("token")
	if getImageToken() != current {
		return "", errors.New("the token you provided isn't correct")
	}
	f, fh, err := r.FormFile("file")
	if err != nil {
		log.Println(err)
		return "", errors.New("couldn't get file from the request")
	}
	defer f.Close()
	err = utilities.ValidateFilenamePngOrJpg(fh.Filename)
	if err != nil {
		log.Println(err)
		return "", errors.New("you entered not valid file")
	}
	res, err := exec.Command("bash", "-c", COMMAND_GENERATING_NAME).Output()
	if err != nil {
		log.Println(err)
		return "", errors.New("couldn't save the file, because there is no more options for filename")
	}
	serverFile, err := ioutil.TempFile(PATH_TO_IMAGES,
		fmt.Sprintf("image.*%s%s", res, filepath.Ext(fh.Filename)))
	if err != nil {
		log.Println(err)
		return "", errors.New("couldn't save the file")
	}
	defer serverFile.Close()
	_, err = io.Copy(serverFile, f)
	if err != nil {
		log.Println(err)
		return "", errors.New("couldn't copy file")
	}
	return filepath.Base(serverFile.Name()), nil
}

func (isi *ImageServiceImpl) DeleteImage(token, filename string) error {
	if token != getImageToken() {
		return errors.New("the token you provided isn't correct")
	}
	err := os.Remove(PATH_TO_IMAGES + filename)
	if err != nil {
		log.Println(err)
		return fmt.Errorf("couldn't delete file: %s", filename)
	}
	return nil
}
