package org.example.a2024_05_sopra_server.apirest

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/airport")
class AirportAPI {

    var tab = Array<PlaneBean?>(5) { null }

    //Méthode qui permet de réinitialiser les données entre 2 tests
    //http://localhost:8080/airport/nextplace
    @GetMapping("/nextplace")
    fun nextplace(): Int {
        println("/nextplace")

        return tab.indexOfFirst { it == null }
    }

    //Méthode qui permet de réinitialiser les données entre 2 tests
    //http://localhost:8080/airport/takeoff
    @GetMapping("/takeoff")
    fun takeoff(position: Int? = null): Int {
        println("/takeoff position=$position")

        if (position == null || position < 0 || position >= tab.size) {
            return 216
        }
        else if (tab[position] == null) {
            return 215
        }

        tab[position] = null
        return 200
    }


    //Méthode qui permet de réinitialiser les données entre 2 tests
    //http://localhost:8080/airport/park
    @PostMapping("/park")
    fun park(@RequestBody planeBean: PlaneBean, position: Int? = null): Int {
        println("/park planeBean=$planeBean")

        if (planeBean.id.isNullOrBlank() || planeBean.name.isNullOrBlank()) {
            return 214
        }
        else if (position == null || position < 0 || position >= tab.size) {
            return 216
        }
        else if (tab[position] != null) {
            return 215
        }

        tab[position] = planeBean
        return 200
    }

    //Méthode qui permet de réinitialiser les données entre 2 tests
    //http://localhost:8080/airport/reset
    @GetMapping("/state")
    fun state() = tab



    //Méthode qui permet de réinitialiser les données entre 2 tests
    //http://localhost:8080/airport/reset
    @GetMapping("/reset")
    fun reset() {
        tab = Array(5) { null }
    }
}

data class PlaneBean(var name: String?, var id: String?)