package br.com.treinaweb.twjobs.core.repositories;

import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.models.Vessel;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

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

    public List<Vessel> vesselsCustom(Long id, Long imo, String categoria, String nome){

        String query = "select A from Vessel as A";
        String condicao = " where ";

        if(id!=null){

            query += condicao + "A.id = :id";
            condicao = " and ";
        }
        if(imo!=null){
            query += condicao + "A.imo = :imo";
            condicao = " and ";
        }
        if(categoria!=null){

            query += condicao + "A.categoria = :categoria";
            condicao = " and ";
        }

        if(nome!=null){

            query += condicao + "A.nome LIKE :nome";
            condicao = " and ";
        }


        var q = em.createQuery(query, Vessel.class);

        if(id!=null){

            q.setParameter("id", id);
        }

        if(imo!=null){

            q.setParameter("imo", imo);
        }
        if(categoria!=null){

            q.setParameter("categoria", categoria);
        }
        if(nome!=null){

            q.setParameter("nome", "%"+nome+"%");
        }


        return q.getResultList();

    }
}
