package com.ispeak.repositorios;

import com.ispeak.entidades.Usuario;
import com.ispeak.enums.Rol;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    @Query("SELECT u FROM Usuario u WHERE u.email LIKE :email")
    public Usuario buscarPorEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.rol LIKE :alumno")
    public List<Usuario> buscarAlumnos(@Param("alumno") Rol rol);

    @Query("SELECT u FROM Usuario u WHERE u.id LIKE :id AND u.rol LIKE :profesor")
    public Usuario buscarProfesor(@Param("id") String id, @Param("profesor") Rol rol);
    
     @Query("SELECT u FROM Usuario u WHERE u.rol LIKE :profesor")
    public List<Usuario> listarProfesor(@Param("profesor") Rol rol);

    @Query("SELECT u FROM Usuario u WHERE u.id LIKE :id AND u.rol LIKE :alumno")
    public Usuario buscarAlumno(@Param("id") String id, @Param("alumno") Rol rol);

    @Query("SELECT u FROM Usuario u WHERE u.baja IS NULL")
    public List<Usuario> buscarActivos();

    
}
