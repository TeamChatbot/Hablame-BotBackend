/**
 * This package contains all Data Transfer Objects, which are expected from a controller.
 * Those DTOs are used to abstract the model classes, because the extern client does not know things like an ID of an object.
 * So create an DTO to each model class which has similar attributes as the model except basic features like the ID 
 * or create time and update time.
 */
package de.fhws.hablame.chatbotbackend.dto;