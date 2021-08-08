package com.example.graphql.scalar.app.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.graphql.scalar.app.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class StudentQueryResolver implements GraphQLQueryResolver {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    EntityManager entityManager;

    public Object getStudentDataById(String studentId) throws JsonProcessingException {
        Map<String, Object> entityData = getEntitiesData(studentId);
        //return mapper.readTree(mapper.writeValueAsString(studentRepository.findById(studentId)));
        return mapper.readTree(mapper.writeValueAsString(entityData));
    }

    public Map<String, Object> getEntitiesData(String studentId) {
        Query getAllColumns = this.entityManager.createNativeQuery("show columns from student");
        List<Object[]> columnsResultList = getAllColumns.getResultList();
        List<String> columnNames = columnsResultList.stream().map(columnName -> String.valueOf(columnName[0])).collect(Collectors.toList());
        String columnNamesString = columnNames.toString().replace("[", "").replace("]", "");
        String queryString = "select " + columnNamesString + " from student where student_id = '" + studentId + "'";
        Query studentByDataQuery = this.entityManager.createNativeQuery(queryString);
        List<Object[]> studentsData = studentByDataQuery.getResultList();
        Map<String, Object> studentsDataMap = new HashMap<>();
        if (ObjectUtils.isNotEmpty(studentsData)) {
            studentsDataMap = IntStream.range(0, studentsData.get(0).length)
                    .mapToObj(i -> CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnNames.get(i)) + "=" + studentsData.get(0)[i])
                    .collect(Collectors.toMap(key -> String.valueOf(key.split("=")[0]), key -> String.valueOf(key.split("=")[1])
                    ));
        }
        return studentsDataMap;
    }
}
