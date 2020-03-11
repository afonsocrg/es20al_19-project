package pt.ulisboa.tecnico.socialsoftware.tutor.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.StudentQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.StudentQuestionDTO;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.StudentQuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class CheckStudentQuestionStatusService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentQuestionRepository studentQuestionRepository;

    public List<StudentQuestionDTO> getAllStudentQuestion(Integer studentId) {

        User user = userRepository.findById(studentId).orElseThrow(() -> new TutorException(USER_NOT_FOUND, studentId));
        if(user.getRole() == User.Role.TEACHER) {
            throw new TutorException(ACCESS_DENIED);
        }
        return studentQuestionRepository.findByUser(studentId).stream().map(StudentQuestionDTO::new).collect(Collectors.toList());
    }

    public List<StudentQuestionDTO> getAllQuestionsWithStatus(Integer studentId, StudentQuestion.SubmittedStatus status) {
        return new ArrayList<StudentQuestionDTO>();
    }

}
