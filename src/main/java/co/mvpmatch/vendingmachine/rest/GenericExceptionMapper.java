package co.mvpmatch.vendingmachine.rest;

import co.mvpmatch.vendingmachine.accesscontrol.AuthorizationFilter;
import co.mvpmatch.vendingmachine.contracts.IDepositService;
import co.mvpmatch.vendingmachine.contracts.IProductService;
import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import co.mvpmatch.vendingmachine.contracts.ITransactionService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
  @Override
  public Response toResponse(Throwable e) {
    Logger.getLogger(GenericExceptionMapper.class.getName()).log(Level.SEVERE, null, e);
    if (e instanceof AuthorizationFilter.VendingMachineUserForbiddenException) {
      return Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IUserService.VendingMachineUserNotFoundException) {
      return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IUserService.VendingMachineUserInternalErrorException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IUserService.VendingMachineCreateUserAlreadyExistsException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IUserService.VendingMachineGetUserForbiddenException) {
      return Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof ITokenSessionService.VendingMachineCreateTokenSessionException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof ITokenSessionService.VendingMachineTokenSessionNotFoundException) {
      return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof ITokenSessionService.VendingMachineDeleteTokenSessionNotFoundException) {
      return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof ITokenSessionService.VendingMachineDeleteTokenSessionException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof ITokenSessionService.VendingMachineReadTokenSessionException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof ITokenSessionService.VendingMachineAuthenticateException) {
      return Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IProductService.VendingMachineCreateProductException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IProductService.VendingMachineReadProductException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IProductService.VendingMachineUpdateProductException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IProductService.VendingMachineProductNotFoundException) {
      return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IProductService.VendingMachineDeleteProductException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IProductService.VendingMachineUpdateProductForbiddenException) {
      return Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IDepositService.VendingMachineDepositException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IDepositService.VendingMachineInvalidDepositException) {
      return Response.status(Response.Status.BAD_REQUEST).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IDepositService.VendingMachineResetDepositException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof ITransactionService.VendingMachineTransactionException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof ITransactionService.VendingMachineDepositTooLowException) {
      return Response.status(Response.Status.BAD_REQUEST).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof ITransactionService.VendingMachineProductCountTooLowException) {
      return Response.status(Response.Status.BAD_REQUEST).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorMessage.create("Something went wrong")).type(MediaType.APPLICATION_JSON).build();
  }
}
