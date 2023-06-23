package br.com.szbetter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.szbetter.model.Usuario;
import br.com.szbetter.model.DiasRegistro;
import br.com.szbetter.factory.ConnectionFactory;

public class DiasRegistroDAO {

    public void save(DiasRegistro diasRegistro) {
        String sql = "INSERT INTO usuario_dias (usuario_id, data, hora_dormir, hora_acordar, qualidade_sono) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
                PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setInt(1, diasRegistro.getUsuario().getId());
            pstm.setDate(2, new java.sql.Date(diasRegistro.getDataRegistro().getTime()));
            pstm.setTime(3, new java.sql.Time(diasRegistro.getHoraDormir().getTime()));
            pstm.setTime(4, new java.sql.Time(diasRegistro.getHoraAcordar().getTime()));
            pstm.setString(5, String.valueOf(diasRegistro.getQualidadeSono()));

            pstm.executeUpdate();

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    diasRegistro.setId(generatedKeys.getInt(1));
                } else {
                    throw new RuntimeException("Falha ao obter o ID do registro de dias após a inserção.");
                }
            }

            System.out.println("Registro de dias cadastrado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DiasRegistro> getRegistrosDias() {
        String sql = "SELECT * FROM usuario_dias";
        List<DiasRegistro> registrosDias = new ArrayList<>();

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
                PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                DiasRegistro registroDias = new DiasRegistro();
                registroDias.setId(rset.getInt("id"));
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                registroDias.setUsuario(usuarioDAO.getUsuarioById(rset.getInt("usuario_id")));
                registroDias.setDataRegistro(rset.getDate("data"));
                registroDias.setHoraDormir(new Date(rset.getTime("hora_dormir").getTime()));
                registroDias.setHoraAcordar(new Date(rset.getTime("hora_acordar").getTime()));
                registroDias.setQualidadeSono(rset.getString("qualidade_sono"));
                registrosDias.add(registroDias);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return registrosDias;
    }

    public DiasRegistro getDiasRegistro(int usuarioId, Date dataRegistro) {
        String sql = "SELECT * FROM usuario_dias WHERE usuario_id = ? AND data = ?";
        DiasRegistro registroDias = null;

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, usuarioId);
            pstm.setDate(2, new java.sql.Date(dataRegistro.getTime()));

            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    registroDias = new DiasRegistro();
                    registroDias.setId(rset.getInt("id"));
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    registroDias.setUsuario(usuarioDAO.getUsuarioById(rset.getInt("usuario_id")));
                    registroDias.setDataRegistro(rset.getDate("data"));
                    registroDias.setHoraDormir(new Date(rset.getTime("hora_dormir").getTime()));
                    registroDias.setHoraAcordar(new Date(rset.getTime("hora_acordar").getTime()));
                    registroDias.setQualidadeSono(rset.getString("qualidade_sono"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return registroDias;
    }

    public void update(DiasRegistro diasRegistro) {
        String sql = "UPDATE usuario_dias SET usuario_id = ?, data = ?, hora_dormir = ?, hora_acordar = ?, qualidade_sono = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, diasRegistro.getUsuario().getId());
            pstm.setDate(2, new java.sql.Date(diasRegistro.getDataRegistro().getTime()));
            pstm.setTime(3, new java.sql.Time(diasRegistro.getHoraDormir().getTime()));
            pstm.setTime(4, new java.sql.Time(diasRegistro.getHoraAcordar().getTime()));
            pstm.setString(5, String.valueOf(diasRegistro.getQualidadeSono()));
            pstm.setInt(6, diasRegistro.getId());

            pstm.executeUpdate();

            System.out.println("Registro de dias atualizado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(DiasRegistro diasRegistro) {
        String sql = "DELETE FROM usuario_dias WHERE id = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, diasRegistro.getId());

            pstm.executeUpdate();

            System.out.println("Registro de dias excluído com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public DiasRegistro getById(int id) {
            String sql = "SELECT * FROM usuario_dias WHERE id = ?";
            DiasRegistro registroDias = null;

            try (Connection conn = ConnectionFactory.createConnectionToMySQL();
                    PreparedStatement pstm = conn.prepareStatement(sql)) {

                pstm.setInt(1, id);

                try (ResultSet rset = pstm.executeQuery()) {
                    if (rset.next()) {
                        registroDias = new DiasRegistro();
                        registroDias.setId(rset.getInt("id"));
                        UsuarioDAO usuarioDAO = new UsuarioDAO();
                        registroDias.setUsuario(usuarioDAO.getUsuarioById(rset.getInt("usuario_id")));
                        registroDias.setDataRegistro(rset.getDate("data"));
                        registroDias.setHoraDormir(new Date(rset.getTime("hora_dormir").getTime()));
                        registroDias.setHoraAcordar(new Date(rset.getTime("hora_acordar").getTime()));
                        registroDias.setQualidadeSono(rset.getString("qualidade_sono"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return registroDias;
        }

    public List<DiasRegistro> getRegistrosSonoByUsuario(Usuario usuario) {
        String sql = "SELECT * FROM usuario_dias WHERE usuario_id = ?";
        List<DiasRegistro> registrosSono = new ArrayList<>();

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, usuario.getId());

            try (ResultSet rset = pstm.executeQuery()) {
                while (rset.next()) {
                    DiasRegistro registroSono = new DiasRegistro();
                    registroSono.setId(rset.getInt("id"));
                    registroSono.setUsuario(usuario);
                    registroSono.setDataRegistro(rset.getDate("data"));
                    registroSono.setHoraDormir(new Date(rset.getTime("hora_dormir").getTime()));
                    registroSono.setHoraAcordar(new Date(rset.getTime("hora_acordar").getTime()));
                    registroSono.setQualidadeSono(rset.getString("qualidade_sono"));
                    registrosSono.add(registroSono);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return registrosSono;
    }
}