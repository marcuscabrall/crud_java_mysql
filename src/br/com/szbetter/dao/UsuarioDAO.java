package br.com.szbetter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.szbetter.factory.ConnectionFactory;
import br.com.szbetter.model.Usuario;

public class UsuarioDAO {

    public void save(Usuario usuario) {
        String sql = "INSERT INTO usuario_info(nome, idade, dataCadastro) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, usuario.getNome());
            pstm.setInt(2, usuario.getIdade());
            pstm.setDate(3, new java.sql.Date(usuario.getDataCadastro().getTime()));

            pstm.execute();
            System.out.println("Usuário salvo com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Usuario usuario) {
        String sql = "UPDATE usuario_info SET nome = ?, idade = ?, dataCadastro = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, usuario.getNome());
            pstm.setInt(2, usuario.getIdade());
            pstm.setDate(3, new java.sql.Date(usuario.getDataCadastro().getTime()));
            pstm.setInt(4, usuario.getId());

            pstm.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteByID(int id) {
        String sql = "DELETE FROM usuario_info WHERE id = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);
            pstm.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Usuario getUsuarioById(int id) {
        String sql = "SELECT * FROM usuario_info WHERE id = ?";
        Usuario usuario = null;

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);

            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    usuario = new Usuario();
                    usuario.setId(rset.getInt("id"));
                    usuario.setNome(rset.getString("nome"));
                    usuario.setIdade(rset.getInt("idade"));
                    usuario.setDataCadastro(rset.getDate("dataCadastro"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuario;
    }
    
    public List<Usuario> getUsuarios() {
        String sql = "SELECT * FROM usuario_info";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rset.getInt("id"));
                usuario.setNome(rset.getString("nome"));
                usuario.setIdade(rset.getInt("idade"));
                usuario.setDataCadastro(rset.getDate("dataCadastro"));

                usuarios.add(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }
}
