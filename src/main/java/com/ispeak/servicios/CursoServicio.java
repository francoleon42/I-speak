package com.ispeak.servicios;

import com.ispeak.entidades.Curso;
import com.ispeak.entidades.Foto;
import com.ispeak.entidades.Usuario;
import com.ispeak.enums.Idioma;
import com.ispeak.enums.Nivel;
import com.ispeak.enums.Turno;
import com.ispeak.repositorios.CursoRepositorio;
import com.ispeak.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CursoServicio {

    private final List<Curso> listaCursos= new ArrayList();
    
    @Autowired
    private CursoRepositorio cursoRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    FotoServicio fotoServicio;

    public Curso crear(MultipartFile archivo, String nombre, Nivel nivel, Idioma idioma, Turno turno, String idProfesor) throws Exception {

        validar(nombre, nivel, idioma);
        Curso curso = new Curso();

        curso.setNombre(nombre);
        curso.setNivel(nivel);
        curso.setIdioma(idioma);
        curso.setTurno(turno);

        Usuario profesor = usuarioServicio.buscarProfesor(idProfesor);
        if (profesor != null) {
            curso.setProfesor(profesor);
        } else {
            throw new Exception("No se pudo encontrar el profesor solicitado");
        }

        curso.setAlta(new Date());
        curso.setBaja(null);
        Foto foto = fotoServicio.guardar(archivo);
        curso.setFoto(foto);

        return cursoRepositorio.save(curso);
    }

    @Transactional(readOnly = true)
    public Curso BuscarId(String id) throws Exception {
        Optional<Curso> respuesta = cursoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new Exception("No ha encontrado el curso");
        }

    }

    public Curso modificar(MultipartFile archivo, String id, String nombre, Nivel nivel, Idioma idioma, Turno turno, String idProfesor) throws Exception {

        validar(nombre, nivel, idioma);

        Optional<Curso> respuesta = cursoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Curso curso = respuesta.get();

            curso.setNombre(nombre);
            curso.setNivel(nivel);
            curso.setIdioma(idioma);
            curso.setTurno(turno);
            Usuario profesor = usuarioServicio.buscarProfesor(idProfesor);
            if (profesor != null) {
                curso.setProfesor(profesor);
            }

            String idFoto = null;
            if (curso.getFoto() != null) {

                idFoto = curso.getFoto().getId();

            }
            Foto foto = fotoServicio.modificar(archivo, idFoto);
            curso.setFoto(foto);

            return cursoRepositorio.save(curso);
        } else {
            throw new Exception("No se pudo encontrar el curso solicitado");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Curso deshabilitar(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("El ID no puede ser nulo");
        }

        Optional<Curso> respuesta = cursoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Curso curso = respuesta.get();
            curso.setBaja(new Date());
            return cursoRepositorio.save(curso);
        } else {
            throw new Exception("No se pudo encontrar el curso solicitado");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Curso habilitar(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("El ID no puede ser nulo");
        }

        Optional<Curso> respuesta = cursoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Curso curso = respuesta.get();
            curso.setBaja(null);
            return cursoRepositorio.save(curso);
        } else {
            throw new Exception("No se pudo encontrar el curso solicitado");
        }
    }

    @Transactional(readOnly = true)
    public List<Curso> listarXnivel(Nivel nivel) throws Exception {
        if (nivel == null) {
            throw new Exception("No leen los niveles");
        } else {
            return cursoRepositorio.buscarPorNivel(nivel);
        }
    }

    @Transactional(readOnly = true)
    public List<Curso> listarXturno(Turno turno) throws Exception {
        if (turno == null) {
            throw new Exception("No leen los turnos");
        } else {
            return cursoRepositorio.buscarPorTurno(turno);
        }

    }
    
    public Curso bucarCursoDeAlumno(String idAlumno) {
        List<Curso> cursos = listarCursos();
        for (Curso curso : cursos) {
            for (Usuario alumno : curso.getAlumnos()) {
                System.out.println(alumno.getNombre());
                if(alumno.getId().equals(idAlumno)) {
                    return curso;
                }
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void añadirAlumno(String id, String idAlumno) throws Exception {

        if (id != null) {
            Curso curso = cursoRepositorio.getById(id);

            Usuario usuario = usuarioRepositorio.getById(idAlumno);

            curso.getAlumnos().add(usuario);

        } else {
            throw new Exception("No existe el curso");
        }

    }

    @Transactional(readOnly = true)
    public List<Curso> listarCursos() {
        return cursoRepositorio.findAll();
    }
 
    @Transactional(readOnly = true)
    public List<Curso> listarCursosPorAlumno() {
        return cursoRepositorio.buscarCursos();
    }
    
     @Transactional(readOnly = true)
    public List<Curso> listarCursosPorProfesor(String idProfesor) {
        return cursoRepositorio.buscarCursosPorProfesor(idProfesor);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarAlumnos(String id) {
        return cursoRepositorio.buscarAlumnos(id);
    }

    @Transactional(readOnly = true)
    public List<Curso> buscarCursosActivos() {
        return cursoRepositorio.buscarActivos();
    }

    private void validar(String nombre, Nivel nivel, Idioma idioma) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("Debe ingresar su nombre");
        }

        if (nivel == null || nivel.toString().trim().isEmpty()) {
            throw new Exception("Debe indicar el nivel del curso");
        }
        if (idioma == null || idioma.toString().trim().isEmpty()) {
            throw new Exception("Debe indicar el idioma del curso");
        }
    }
}
