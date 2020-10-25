package main
import("strings")
var bases = [...]string{"Abkhazia", "Akrotiri and Dhekelia", "Alderney",
	"Andorra", "Australia", "Austria", "Belgium", "Brazil",
	"Bulgaria", "Canada", "China", "Cocos (Keeling) Islands",
	"Cook Islands", "Croatia", "Cyprus", "Czech Republic",
	"Denmark", "Estonia", "Faroe Islands", "Finland",
	"France", "Germany", "Greece", "Guernsey", "Hong Kong",
	"Hungary", "India", "Indonesia", "Ireland", "Isle of Man",
	"Israel", "Italy", "Japan", "Jersey", "Kiribati", "Kosovo",
	"Latvia", "Liechtenstein", "Lithuania", "Luxembourg", "Malaysia",
	"Malta", "Mexico", "Monaco", "Montenegro", "Nauru", "Netherlands",
	"New Zealand", "Niue", "Northern Cyprus", "Norway", "Palestine",
	"Philippines", "Pitcairn Islands", "Poland", "Portugal", "Romania",
	"Russia", "San Marino", "Singapore", "Slovakia", "Slovenia", "South Africa",
	"South Georgia and the South Sandwich Islands", "South Ossetia", "Spain",
	"Sweden", "Switzerland", "Thailand", "Turkey", "Tuvalu", "United Kingdom", "Vatican City"}

func checkCountry(base string) (string,bool) {

	for index, _ := range bases {
		if strings.ToLower(base) == strings.ToLower(bases[index]) {
			return bases[index],true
		}
	}
	return "",false
}
func assignCurr(country string) string {
	var curr string
	switch country {
	case "abkhazia":
		curr = "RUB"
	case "Akrotiri and Dhekelia":
		curr = "EUR"
	case "Alderney":
		curr = "GBP"
	case "Andorra":
		curr = "EUR"
	case "Australia":
		curr = "AUD"
	case "Austria":
		curr = "EUR"
	case "Belgium":
		curr = "EUR"
	case "Brazil":
		curr = "BRL"
	case "Bulgaria":
		curr = "BGN"
	case "Canada":
		curr = "CAD"
	case "China":
		curr = "CNY"
	case "Cocos (Keeling) Islands":
		curr = "AUD"
	case "Cook Islands":
		curr = "NZD"
	case "Croatia":
		curr = "HRK"
	case "Cyprus":
		curr = "EUR"
	case "Czech Republic":
		curr = "CZK"
	case "Denmark":
		curr = "DKK"
	case "Estonia":
		curr = "EUR"
	case "Faroe Islands":
		curr = "DKK"
	case "Finland":
		curr = "EUR"
	case "France":
		curr = "EUR"
	case "Germany":
		curr = "EUR"
	case "Greece":
		curr = "EUR"
	case "Guernsey":
		curr = "GBP"
	case "Hong Kong":
		curr = "HKD"
	case "Hungary":
		curr = "HUF"
	case "India":
		curr = "INR"
	case "Indonesia":
		curr = "IDR"
	case "Ireland":
		curr = "EUR"
	case "Isle of Man":
		curr = "GBP"
	case "Israel":
		curr = "ILS"
	case "Italy":
		curr = "EUR"
	case "Japan":
		curr = "JPY"
	case "Jersey":
		curr = "GBP"
	case "Kiribati":
		curr = "AUD"
	case "Kosovo":
		curr = "EUR"
	case "Latvia":
		curr = "EUR"
	case "Liechtenstein":
		curr = "CHF"
	case "Lithuania":
		curr = "EUR"
	case "Luxembourg":
		curr = "EUR"
	case "Malaysia":
		curr = "MYR"
	case "Malta":
		curr = "EUR"
	case "Mexico":
		curr = "MXN"
	case "Monaco":
		curr = "EUR"
	case "Montenegro":
		curr = "EUR"
	case "Nauru":
		curr = "AUD"
	case "Netherlands":
		curr = "EUR"
	case "New Zealand":
		curr = "NZD"
	case "Niue":
		curr = "NZD"
	case "Northern Cyprus":
		curr = "TRY"
	case "Norway":
		curr = "NOK"
	case "Palestine":
		curr = "ILS"
	case "Philippines":
		curr = "PHP"
	case "Pitcairn Islands":
		curr = "NZD"
	case "Poland":
		curr = "PLN"
	case "Portugal":
		curr = "EUR"
	case "Romania":
		curr = "RON"
	case "Russia":
		curr = "RUB"
	case "San Marino":
		curr = "EUR"
	case "Singapore":
		curr = "SGD"
	case "Slovakia":
		curr = "EUR"
	case "Slovenia":
		curr = "EUR"
	case "South Africa":
		curr = "ZAR"
	case "South Georgia and the South Sandwich Islands":
		curr = "GBP"
	case "South Ossetia":
		curr = "RUB"
	case "Spain":
		curr = "EUR"
	case "Sweden":
		curr = "SEK"
	case "Switzerland":
		curr = "CHF"
	case "Thailand":
		curr = "THB"
	case "Turkey":
		curr = "TRY"
	case "Tuvalu":
		curr = "AUD"
	case "United Kingdom":
		curr = "GBP"
	case "Vatican City":
		curr = "EUR"

	}
	return curr
}

