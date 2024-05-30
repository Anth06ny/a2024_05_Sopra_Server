package org.example.a2024_05_sopra_server.services

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service
class TeacherService(val teacherRepository: TeacherRepository){

    fun createTeacher(name:String?, code:Int) : TeacherBean {
        if(name.isNullOrBlank()){
            throw Exception("Name missing")
        }
        else if(code !in 1..10){
            throw Exception("Code incorrecte")
        }
        val teacher = TeacherBean(null, name, code)
        teacherRepository.save(teacher)
        return teacher
    }

    fun getAll() = teacherRepository.findAll()
}

interface TeacherRepository : JpaRepository<TeacherBean, Long> {

}

@Entity
@Table(name = "teacher")
data class TeacherBean(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String = "",
    @Column(name = "code")
    var code: Int? = null
)