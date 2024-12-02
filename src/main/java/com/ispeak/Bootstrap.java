package com.ispeak.config;

import com.ispeak.entidades.Curso;
import com.ispeak.entidades.Foto;
import com.ispeak.entidades.Usuario;
import com.ispeak.enums.Idioma;
import com.ispeak.enums.Nivel;
import com.ispeak.enums.Rol;
import com.ispeak.enums.Turno;
import com.ispeak.repositorios.CursoRepositorio;
import com.ispeak.repositorios.FotoRepositorio;
import com.ispeak.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Bootstrap implements ApplicationRunner {
    private  final CursoRepositorio cursoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final FotoRepositorio fotoRepositorio;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Crear usuarios
        Usuario admin = new Usuario();
        admin.setNombre("Admin");
        admin.setApellido("Admin");
        admin.setRol(Rol.ADMIN);
        admin.setDni(12345678);
        admin.setEmail("admin@correo.com");
        admin.setPassword("admin123");
        admin.setAlta(new Date());
        usuarioRepositorio.save(admin);

        Usuario estudiante = new Usuario();
        estudiante.setNombre("Juan");
        estudiante.setApellido("Pérez");
        estudiante.setRol(Rol.ALUMNO);
        estudiante.setDni(87654321);
        estudiante.setEmail("juan@correo.com");
        estudiante.setPassword("estudiante123");
        estudiante.setAlta(new Date());
        usuarioRepositorio.save(estudiante);

        // Crear una foto
        Foto foto = new Foto();
        foto.setNombre("Curso1.png");
        foto.setMime("image/png");
        foto.setContenido(new byte[]{});
        fotoRepositorio.save(foto);

        // Crear un curso
        Curso curso = new Curso();
        curso.setNombre("Curso de Java");
        curso.setNivel(Nivel.INTERMEDIO_B1);
        curso.setIdioma(Idioma.ESPAÑOL);
        curso.setTurno(Turno.MAÑANA);
        curso.setAlta(new Date());
        curso.setProfesor(admin);
        curso.setAlumnos(List.of(estudiante));
        curso.setFoto(foto);
        cursoRepositorio.save(curso);

        // Mensaje de confirmación
        System.out.println("Datos iniciales cargados correctamente.");
    }
}
