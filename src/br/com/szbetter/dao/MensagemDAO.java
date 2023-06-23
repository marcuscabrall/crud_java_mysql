package br.com.szbetter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import br.com.szbetter.factory.ConnectionFactory;
import br.com.szbetter.model.Mensagem;

public class MensagemDAO {

    public void save(Mensagem mensagem) {
        String sql = "INSERT INTO mensagem (texto, link) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, mensagem.getTexto());
            pstm.setString(2, mensagem.getLink());

            pstm.executeUpdate();

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mensagem.setId(generatedKeys.getInt(1));
                } else {
                    throw new RuntimeException("Falha ao obter o ID da mensagem após a inserção.");
                }
            }

            System.out.println("Mensagem cadastrada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mensagem getRandomMensagem() {
        String sql = "SELECT * FROM mensagem ORDER BY RAND() LIMIT 1";
        Mensagem mensagem = null;

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            if (rset.next()) {
                mensagem = new Mensagem();
                mensagem.setId(rset.getInt("id"));
                mensagem.setTexto(rset.getString("texto"));
                mensagem.setLink(rset.getString("link"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mensagem;
    }

    public List<Mensagem> getMensagens() {
        String sql = "SELECT * FROM mensagem";
        List<Mensagem> mensagens = new ArrayList<>();

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                Mensagem mensagem = new Mensagem();
                mensagem.setId(rset.getInt("id"));
                mensagem.setTexto(rset.getString("texto"));
                mensagem.setLink(rset.getString("link"));

                mensagens.add(mensagem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mensagens;
    }
}