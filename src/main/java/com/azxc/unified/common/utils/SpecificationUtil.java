package com.azxc.unified.common.utils;

import java.util.Date;
import org.hibernate.mapping.Collection;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification工具类
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
public final class SpecificationUtil {

  /**
   * like
   */
  public static <T> Specification<T> containsLike(String attribute, String value) {
    return (root, query, cb) -> cb.like(root.get(attribute), "%" + value + "%");
  }

  /**
   * eq
   */
  public static <T> Specification<T> equals(String attribute, String value) {
    return (root, query, cb) -> cb.equal(root.get(attribute), value);
  }

  /**
   * between
   */
  public static <T> Specification<T> between(String attribute, int min, int max) {
    return (root, query, cb) -> cb.between(root.get(attribute), min, max);
  }

  public static <T> Specification<T> between(String attribute, double min, double max) {
    return (root, query, cb) -> cb.between(root.get(attribute), min, max);
  }

  public static <T> Specification<T> between(String attribute, Date min, Date max) {
    return (root, query, cb) -> cb.between(root.get(attribute), min, max);
  }

  /**
   * in
   */
  public static <T> Specification<T> in(String attribute, Collection values) {
    return (root, query, cb) -> root.get(attribute).in(values);
  }

  /**
   * >
   */
  public static <T> Specification<T> greaterThan(String attribute, String value) {
    return (root, query, cb) -> cb.greaterThan(root.get(attribute), value);
  }

  public static <T> Specification<T> greaterThan(String attribute, double value) {
    return (root, query, cb) -> cb.greaterThan(root.get(attribute), value);
  }

  public static <T> Specification<T> greaterThan(String attribute, int value) {
    return (root, query, cb) -> cb.greaterThan(root.get(attribute), value);
  }

  public static <T> Specification<T> greaterThan(String attribute, Date value) {
    return (root, query, cb) -> cb.greaterThan(root.get(attribute), value);
  }

  /**
   * <
   */
  public static <T> Specification<T> lessThan(String attribute, String value) {
    return (root, query, cb) -> cb.lessThan(root.get(attribute), value);
  }

  public static <T> Specification<T> lessThan(String attribute, double value) {
    return (root, query, cb) -> cb.lessThan(root.get(attribute), value);
  }

  public static <T> Specification<T> lessThan(String attribute, int value) {
    return (root, query, cb) -> cb.lessThan(root.get(attribute), value);
  }

  public static <T> Specification<T> lessThan(String attribute, Date value) {
    return (root, query, cb) -> cb.lessThan(root.get(attribute), value);
  }

}
