package com.aninfo.service;

import com.aninfo.exceptions.*;
import com.aninfo.model.Account;
import com.aninfo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Collection<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long cbu) {
        return accountRepository.findById(cbu);
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public void deleteById(Long cbu) {
        accountRepository.deleteById(cbu);
    }

    @Transactional
    public Account withdraw(Long cbu, Double sum) {
        Account account = accountRepository.findAccountByCbu(cbu);

        if (account.getBalance() < sum) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - sum);
        accountRepository.save(account);

        return account;
    }

    @Transactional
    public Account deposit(Long cbu, Double sum) {

        if (sum <= 0) {
            throw new DepositNegativeSumException("Cannot deposit negative sums");
        }

        Account account = accountRepository.findAccountByCbu(cbu);
        account.setBalance(account.getBalance() + sum);
        accountRepository.save(account);

        return account;
    }

    public boolean depositar(Account account, Double importe) {

        if (importe <= 0) {
            if (importe == 0)
                throw new DepositoNuloException("No se puede depositar un importe nulo.");
            throw new DepositNegativeSumException("No se puede depositar un importe negativo.");
        }

        double extra = 0;
        if(importe > 2000) extra = importe * 0.1;
        if(extra > 500) extra = 500;

        importe = importe + extra;

        account.setBalance(account.getBalance() + importe);
        accountRepository.save(account);

        return true;
    }

    public boolean extraer(Account account, Double importe) {
        if (importe <= 0) {
            if (importe == 0)
                throw new DepositoNuloException("No se puede extraer un importe nulo.");
            throw new DepositNegativeSumException("No se puede extraer un importe negativo.");
        }

        if (account.getBalance() < importe) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - importe);
        accountRepository.save(account);

        return true;
    }

    public Double getSaldo(Account account) {
        return account.getBalance();
    }

}
