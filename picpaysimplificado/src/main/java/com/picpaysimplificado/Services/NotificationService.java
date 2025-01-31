package com.picpaysimplificado.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.picpaysimplificado.DTO.NotificationDTO;
import com.picpaysimplificado.Entities.User;
import com.picpaysimplificado.Exceptions.NotificationException;

@Service
public class NotificationService {
    
    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message){
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        // ResponseEntity<String> notificationResponse = restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", notificationRequest, String.class);

        // if(notificationResponse.getStatusCode() == HttpStatus.OK){
        //     System.out.println("Erro ao enviar notificação");
        //     throw new NotificationException("Serviço de notificação está fora do ar. ");
            
        // }

      System.out.println("Notificação enviada para o usuário.");
    }
}
