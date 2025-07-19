package br.com.treinaweb.twjobs.core.repositories;

import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import br.com.treinaweb.twjobs.core.models.Accept;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class AcceptCustomRepository {

     private final EntityManager em;

     public AcceptCustomRepository(EntityManager em) {
          this.em = em;
     }

     public List<Accept> acceptsCustom(Long id, String imo, String status, String nome, String categoria, LocalDate dataInicio, LocalDate dataFim) {
          String query = "select A from Accept as A";
          String condicao = " where ";

          if (id != null) {
               query += condicao + "A.id = :id";
               condicao = " and ";
          }
          if (imo != null) {
               query += condicao + "A.imo = :imo";
               condicao = " and ";
          }
          if (status != null) {
               query += condicao + "A.status = :status";
               condicao = " and ";
          }
          if (nome != null) {
               query += condicao + "A.nome LIKE :nome";
               condicao = " and ";
          }
          if (categoria != null) {
               query += condicao + "A.categoria = :categoria";
               condicao = " and ";
          }

          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

          if (dataInicio != null && dataFim != null) {
               query += condicao + "A.data_create BETWEEN :dataInicio AND :dataFim";
          } else if (dataInicio != null) {
               query += condicao + "A.data_create >= :dataInicio";
          } else if (dataFim != null) {
               query += condicao + "A.data_create <= :dataFim";
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

          if (dataInicio != null && dataFim != null) {
               q.setParameter("dataInicio", dataInicio.format(formatter));
               q.setParameter("dataFim", dataFim.format(formatter));
          } else if (dataInicio != null) {
               q.setParameter("dataInicio", dataInicio.format(formatter));
          } else if (dataFim != null) {
               q.setParameter("dataFim", dataFim.format(formatter));
          }

          return q.getResultList();
     }
}
