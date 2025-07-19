package br.com.treinaweb.twjobs.core.repositories;

import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.models.Vessel;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class VesselCustomRepository {

    private final EntityManager em;

    public VesselCustomRepository(EntityManager em) {
        this.em = em;
    }

//    id
//    imo
//    status
//    nome

    public List<Vessel> vesselsCustom(Long id, Long imo, String categoria, String nome, LocalDate dataInicio, LocalDate dataFim) {

        String query = "select A from Vessel as A";
        String condicao = " where ";

        if (id != null) {
            query += condicao + "A.id = :id";
            condicao = " and ";
        }

        if (imo != null) {
            query += condicao + "A.imo = :imo";
            condicao = " and ";
        }

        if (categoria != null) {
            query += condicao + "A.categoria = :categoria";
            condicao = " and ";
        }

        if (nome != null) {
            query += condicao + "A.nome LIKE :nome";
            condicao = " and ";
        }

        if (dataInicio != null && dataFim != null) {
            query += condicao + "A.time_create BETWEEN :dataInicio AND :dataFim";
        } else if (dataInicio != null) {
            query += condicao + "A.time_create >= :dataInicio";
        } else if (dataFim != null) {
            query += condicao + "A.time_create <= :dataFim";
        }

        var q = em.createQuery(query, Vessel.class);

        if (id != null) {
            q.setParameter("id", id);
        }

        if (imo != null) {
            q.setParameter("imo", imo);
        }

        if (categoria != null) {
            q.setParameter("categoria", categoria);
        }

        if (nome != null) {
            q.setParameter("nome", "%" + nome + "%");
        }

        if (dataInicio != null) {
            q.setParameter("dataInicio", dataInicio.atStartOfDay());
        }

        if (dataFim != null) {
            q.setParameter("dataFim", dataFim.atTime(23, 59, 59));
        }

        return q.getResultList();
    }

}
