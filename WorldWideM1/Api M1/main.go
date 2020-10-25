package main

import (
	"fmt"
	"log"
	"os"
	"strings"
	"github.com/ramin0/chatbot"
	"net/http"
	"time"
	"encoding/json"
	"io/ioutil"
	"strconv"
	"errors"
	
)

// Autoload environment variables in .env
import _ "github.com/joho/godotenv/autoload"

var (
	instructions = "To change currency from your source to dest just write the amount you want to change.\nTo change source country write \"source: <country>\"\nTo change destination country  write \"dest: <country>\" wihtout quotes"
)
//Rate structure contains float attributes of most of currencires
type Rates struct {
	AUD float64 `json:"AUD"`
	BGN float64 `json:"BGN"`
	BRL float64 `json:"BRL"`
	CAD float64 `json:"CAD"`
	CHF float64 `json:"CHF"`
	CNY float64 `json:"CNY"`
	CZK float64 `json:"CZK"`
	DKK float64 `json:"DKK"`
	GBP float64 `json:"GBP"`
	HKD float64 `json:"HKD"`
	HRK float64 `json:"HRK"`
	HUF float64 `json:"HUF"`
	IDR float64 `json:"IDR"`
	ILS float64 `json:"ILS"`
	INR float64 `json:"INR"`
	JPY float64 `json:"JPY"`
	KRW float64 `json:"KRW"`
	MXN float64 `json:"MXN"`
	MYR float64 `json:"MYR"`
	NOK float64 `json:"NOK"`
	NZD float64 `json:"NZD"`
	PHP float64 `json:"PHP"`
	PLN float64 `json:"PLN"`
	RON float64 `json:"RON"`
	RUB float64 `json:"RUB"`
	SEK float64 `json:"SEK"`
	SGD float64 `json:"SGD"`
	THB float64 `json:"THB"`
	TRY float64 `json:"TRY"`
	USD float64 `json:"USD"`
	ZAR float64 `json:"ZAR"`
	EUR float64 `json:"EUR"`
}
type worldWindow struct {
	Base  string `json:"base"`
	Date  string `json:"date"`
	Rates Rates  `json:"rates"`
}
//Take the base countery which you are from and the countery u want ask about and the amount of money you want to change
func getChange(countery1 string, countery2 string, amount float64) (float64,error) {
	var value float64
	//assigncurr it is in the countries.go file it assign the countery it`s currency as we compare currencies
	base := assignCurr(countery1)
	curr := assignCurr(countery2)

//Here we send the request to API with the base countery
	req, err := http.NewRequest(http.MethodGet,"https://api.fixer.io/latest?base=" + base, nil)

	if err != nil {
		return 0.0,err
	}
    Client := http.Client{
		Timeout: time.Second * 3,
	}
	res, getErr := Client.Do(req)
	if getErr != nil {
		return 0.0,getErr
	}

	body, readErr := ioutil.ReadAll(res.Body)
	if readErr != nil {
		return 0.0,readErr
	}

	client:= worldWindow{}
	jsonErr := json.Unmarshal(body, &client)
	if jsonErr != nil {
		return 0.0,jsonErr
	}
	//in case if the base and desired countery both has same currency
	if curr == base{
		return amount,nil}
	//getting the rate between base and other countery 
	switch curr {
	case "AUD":
		value = client.Rates.AUD
	case "BGN":
		value = client.Rates.BGN
	case "BRL":
		value = client.Rates.BRL
	case "CAD":
		value = client.Rates.CAD
	case "CHF":
		value = client.Rates.CHF
	case "CNY":
		value = client.Rates.CNY
	case "CZK":
		value = client.Rates.CZK
	case "DKK":
		value = client.Rates.DKK
	case "GBP":
		value = client.Rates.GBP
	case "HKD":
		value = client.Rates.HKD
	case "HRK":
		value = client.Rates.HRK
	case "HUF":
		value = client.Rates.HUF
	case "IDR":
		value = client.Rates.IDR
	case "ILS":
		value = client.Rates.ILS
	case "INR":
		value = client.Rates.INR
	case "JPY":
		value = client.Rates.JPY
	case "KRW":
		value = client.Rates.KRW
	case "MXN":
		value = client.Rates.MXN
	case "MYR":
		value = client.Rates.MYR
	case "NOK":
		value = client.Rates.NOK
	case "NZD":
		value = client.Rates.NZD
	case "PHP":
		value = client.Rates.PHP
	case "PLN":
		value = client.Rates.PLN
	case "RON":
		value = client.Rates.RON
	case "RUB":
		value = client.Rates.RUB
	case "SEK":
		value = client.Rates.SEK
	case "SGD":
		value = client.Rates.SGD
	case "THB":
		value = client.Rates.THB
	case "TRY":
		value = client.Rates.TRY
	case "ZAR":
		value = client.Rates.ZAR
	case "EUR":
		value = client.Rates.EUR

	}
   //returning the difference
	return (value * amount),nil

}
func chatbotProcess(session chatbot.Session, message string) (string, error) {
//  	if strings.EqualFold(message, "chatbot") {
//  		return "", fmt.Errorf("This can't be, I'm the one and only %s!", message)
//  }
	
	_, nameFound := session["name"]
	
	if(!nameFound){ // still didn't get the name
		session["name"] = message
		return fmt.Sprintf("Hello <b>%s</b>, Where are you right now?", message), nil
	}
	
	_, srcConFound := session["srcCountry"]
	if(!srcConFound){ // still didn't get source country
		// check message contains valid country
		country:= message
		country,valid := checkCountry(country)
		if(valid){
			session["srcCountry"] =  country
			return fmt.Sprintf("So you are from %s. What country are you going to?", country), nil
			
		}else{
			return fmt.Sprintf("Invalid or inavailable Country. Please respond with a valid one"), nil
		}
	}
	
	_, destConFound := session["destCountry"]
	if(!destConFound){ // still didn't get source country
		// check message contains valid country
		country:= message
		country,valid := checkCountry(country)
		if(valid){
			session["destCountry"] =  country
			return fmt.Sprintf("So you are going to %s.\n%s", country, instructions), nil
		}else{
			
			return fmt.Sprintf("Invalid or inavailable country. Please respond with a valid one"), nil
		}	
	}
	
	num, err := strconv.ParseFloat(message, 64)
	if(err == nil){
		afterChange := 0.0;
		srcCountry,_ := session["srcCountry"].(string)
		destCountry,_ := session["destCountry"].(string)
		afterChange,err = getChange(srcCountry,destCountry,num)
		if(err != nil){
			err = errors.New("Something went wrong. Please try again in 30 seconds")
		}
		
		return fmt.Sprintf("%.3f in %s is %.3f in %s ", num,srcCountry,afterChange,destCountry), err
	}
	
	if(strings.Contains(message,"source")){
		country := message[8:]
		country,valid := checkCountry(country)
		if(valid){
			session["srcCountry"] =  country
			return fmt.Sprintf("source country changed successfully to %s.", country), nil
			
		}else{
			return fmt.Sprintf("Invalid or inavailable Country."), nil
		}
	}
	if(strings.Contains(message,"dest")){
		country := message[6:]
		country,valid := checkCountry(country)
		if(valid){
			session["destCountry"] =  country
			return fmt.Sprintf("destination country changed successfully to %s.", country), nil
			
		}else{
			return fmt.Sprintf("Invalid or inavailable Country."), nil
		}
	}
	
	return fmt.Sprintf(instructions),nil
	
	
}



func main() {
	// Uncomment the following lines to customize the chatbot
	chatbot.WelcomeMessage = "What's your name?"
	chatbot.ProcessFunc(chatbotProcess)

	// Use the PORT environment variable
	port := os.Getenv("PORT")
	// Default to 3000 if no PORT environment variable was defined
	if port == "" {
		port = "3000"
	}

	// Start the server
	fmt.Printf("Listening on port %s...\n", port)
	log.Fatalln(chatbot.Engage(":" + port))
}


