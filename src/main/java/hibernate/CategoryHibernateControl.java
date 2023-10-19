package hibernate;

import model.Accounting;
import model.Category;
import model.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class CategoryHibernateControl {
    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public CategoryHibernateControl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void create(Category category) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategory(category.getId()) != null){
                throw new Exception("Category " + category + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void createSubCategory(Category category, Category parentCategory) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            parentCategory = em.find(Category.class, parentCategory.getId());
            parentCategory.getSubCategories().add(category);
            em.merge(parentCategory);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategory(category.getId()) != null) {
                throw new Exception("Category " + category.getName() + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Category category) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            category = em.merge(category);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = category.getId();
                if (findCategory(id) == null) {
                    throw new Exception("The category with name " + category.getName() + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category category;
            try {
                category = em.getReference(Category.class, id);
                category.getId();
                for (Category s : category.getSubCategories()) {
                    s.getSubCategories().remove(category);
                    destroy(s.getId());
                }
                category.getSubCategories().clear();
                em.merge(category);
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The category with id " + id + " no longer exists.", enfe);
            }
            em.remove(category);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Category findCategory(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public void addTransactionToCategory(Category category, Transaction transaction) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                category = em.find(Category.class, category.getId());
                category.getTransactions().add(transaction);
                em.persist(category);
                em.merge(category);
                em.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeTransactionFromCategory(Category category, Transaction transaction) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                category = em.find(Category.class, category.getId());
                category.removeTransaction(transaction.getId());
                em.merge(category);
                em.flush();

            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing Transaction from Category section list", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Category.class));
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

    public Category findCategory(String name) {
        List<Category> categories = findCategoryEntities();
        for (Category c : categories) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

}
