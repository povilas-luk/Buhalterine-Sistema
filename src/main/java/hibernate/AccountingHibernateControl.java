package hibernate;

import model.Accounting;
import model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class AccountingHibernateControl {
    private EntityManagerFactory emf;

    public AccountingHibernateControl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Accounting accounting) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(accounting);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAccounting(accounting.getAccName()) != null) {
                throw new Exception("Accounting " + accounting + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Accounting accounting) throws  Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.flush();
            accounting = em.merge(accounting);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String name = accounting.getAccName();
                if (findAccounting(name) == null) {
                    throw new Exception("The Accounting with id " + accounting + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String name) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Accounting accounting;
            try {
                accounting = em.getReference(Accounting.class, name);
                accounting.getAccName();
                accounting.getUsers().clear();
                em.merge(accounting);
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The Accounting with name " + name + " no longer exists.", enfe);
            }
            em.remove(accounting);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Accounting findAccounting(String name) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Accounting.class, name);
        } finally {
            em.close();
        }
    }

    public void addUserToAccounting(Accounting accounting, User user) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                accounting.getUsers().add(user);
                em.merge(accounting);
                em.flush();
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Consumer already exits!");
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeUserFromAccounting(Accounting accounting, User user) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                accounting.getUsers().remove(user);
                em.merge(accounting);
                em.flush();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing consumer", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Accounting> findAccountingEntities() {
        return findAccountingEntities(true, -1, -1);
    }

    public List<Accounting> findAccountingEntities(int maxResults, int firstResult) {
        return findAccountingEntities(false, maxResults, firstResult);
    }

    private List<Accounting> findAccountingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Accounting.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /*public void addConsumerToAccounting(Accounting accounting, Consumer consumer) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                //consumer.setAccounting(accounting);
                //accounting.getConsumers().add(consumer);
                em.merge(accounting);
                em.flush();
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Consumer already exits!");
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void addCompanyToAccounting(Accounting accounting, Company company) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                company.setAccounting(accounting);
                accounting.getCompanies().add(company);
                em.merge(accounting);
                em.flush();
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Company already exits!");
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }*/


    /*public void removeConsumerFromAccounting(Accounting accounting, Consumer consumer) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                accounting.getConsumers().remove(consumer);
                em.merge(accounting);
                em.flush();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing consumer", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeCompanyFromAccounting(Accounting accounting, Company company) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                accounting.getCompanies().remove(company);
                em.merge(accounting);
                em.flush();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing company", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }*/

}
