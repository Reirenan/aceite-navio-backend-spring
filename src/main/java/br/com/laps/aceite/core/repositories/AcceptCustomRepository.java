package br.com.laps.aceite.core.repositories;

import br.com.laps.aceite.core.models.Accept;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class AcceptCustomRepository {

     private final EntityManager em;

     public AcceptCustomRepository(EntityManager em) {
          this.em = em;
     }

     public List<Accept> acceptsCustom(Long id, String imo, String status, String nome, String categoria,
               LocalDate dataInicio, LocalDate dataFim) {
          String query = "select A from Accept as A join A.vessel V";
          String condicao = " where ";

          if (id != null) {
               query += condicao + "A.id = :id";
               condicao = " and ";
          }
          if (imo != null) {
               query += condicao + "V.imo = :imo";
               condicao = " and ";
          }
          if (status != null) {
               query += condicao + "A.status = :status";
               condicao = " and ";
          }
          if (nome != null) {
               query += condicao + "V.nome LIKE :nome";
               condicao = " and ";
          }
          if (categoria != null) {
               query += condicao + "V.categoria = :categoria";
               condicao = " and ";
          }
          if (dataInicio != null) {
               query += condicao + "CAST(A.dataHoraAccept AS string) LIKE :data_accept";
               condicao = " and ";
          }

          var q = em.createQuery(query, Accept.class);

          if (id != null) {
               q.setParameter("id", id);
          }
          if (imo != null) {
               q.setParameter("imo", imo);
          }
          if (status != null) {
               q.setParameter("status", status);
          }
          if (nome != null) {
               q.setParameter("nome", "%" + nome + "%");
          }
          if (categoria != null) {
               q.setParameter("categoria", categoria);
          }
          if (dataInicio != null) {
               q.setParameter("data_accept", "%" + dataInicio + "%");
          }

          return q.getResultList();
     }
}
