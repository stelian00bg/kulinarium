package utilities

import "fmt"

func GetLocalAdress(port int) (string, error) {
	err := ValidatePort(port)
	if err != nil {
		return "", err
	}
	return fmt.Sprintf(":%d", port), nil
}
