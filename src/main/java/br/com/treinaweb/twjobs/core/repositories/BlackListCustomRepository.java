package br.com.treinaweb.twjobs.core.repositories;

import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.models.BlackList;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlackListCustomRepository {

    private final EntityManager em;

    public BlackListCustomRepository(EntityManager em) {
        this.em = em;
    }


//    id
//    imo
//    data_create
//    time_create

    public List<BlackList> blackListedsCustom(Long id, String imo, String data_create, String time_create){

        String query = "select A from BlackList as A";
        String condicao = " where ";

        if(id!=null){

            query += condicao + "A.id = :id";
            condicao = " and ";
        }
        if(imo!=null){
            query += condicao + "A.imo = :imo";
            condicao = " and ";
        }
        if(data_create!=null){

            query += condicao + "A.data_create LIKE :data_create";
            condicao = " and ";
        }

        if(time_create!=null){

            query += condicao + "A.time_create = :time_create";
            condicao = " and ";
        }


        var q = em.createQuery(query, BlackList.class);

        if(id!=null){

            q.setParameter("id", id);
        }

        if(imo!=null){

            q.setParameter("imo", imo);
        }
        if(data_create!=null){

            q.setParameter("data_create", "%"+data_create+"%");
        }
        if(time_create!=null){

            q.setParameter("time_create", "%"+time_create+"%");
        }


        return q.getResultList();

    }



}
