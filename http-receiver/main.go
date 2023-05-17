package main

import (
	"fmt"
	"io"
	"net/http"
	"sync"
)

func postNotification(port uint) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		body, _ := io.ReadAll(r.Body)

		fmt.Printf("Received notification on port %d: %s\n", port, string(body))

		w.WriteHeader(200)
	}
}

func startServer(port uint) {
	router := http.NewServeMux()

	router.HandleFunc("/notification", postNotification(port))

	fmt.Printf("Starting server on port %d\n", port)
	err := http.ListenAndServe(fmt.Sprintf(":%d", port), router)

	if err != nil {
		fmt.Printf("Error starting server on port %d: %s", port, err.Error())
	}
}

func main() {
	ports := []uint{
		8080,
		8081,
		8082,
	}

	var wg sync.WaitGroup

	for _, port := range ports {
		wg.Add(1)
		port := port

		go func() {
			defer wg.Done()
			startServer(port)
		}()
	}

	wg.Wait()
}
