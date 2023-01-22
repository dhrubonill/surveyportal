package com.example.abc.controller;

import com.example.abc.entity.ClientBank;
import com.example.abc.enums.ClientStatus;
import com.example.abc.repository.ClientBankRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Controller
public class BulkDataUploadRestController {
    private final ClientBankRepository clientBankRepository;

    public BulkDataUploadRestController(ClientBankRepository clientBankRepository) {
        this.clientBankRepository = clientBankRepository;
    }

    @GetMapping("/bulk-data-upload")
    public String bulkUpload(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "bulk-data-upload";
    }

    @GetMapping("/agent-view")
    public String fetchClientBankForAgent(Model model){
        List<ClientBank> list = clientBankRepository.findAll().stream().toList();
        model.addAttribute("clients", list);
        List<String> options = new ArrayList<>();
        for(ClientStatus i: ClientStatus.values()){
            options.add(i.name());
        }
        model.addAttribute("options", options);
        return "agent-view";
    }

    @PostMapping("/update")
    public String updateById(@RequestParam(value = "id", required = true) Long id,
                             @ModelAttribute("client") ClientBank client){
        ClientBank clientBank = clientBankRepository.findById(id).orElse(new ClientBank());
        clientBank.setStatus(client.getStatus());
        clientBank.setMobileNo(client.getMobileNo());
        clientBank.setFullName(client.getFullName());
        clientBank.setEmail(client.getEmail());
        clientBankRepository.save(client);
        return "agent-view";
    }

    @PostMapping("/upload")
    public String bulkUpload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File createdFile = convertMultiPartToFile(multipartFile);
        FileInputStream file =  new FileInputStream(createdFile);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        int nameColumnIndex=-1;
        int emailColumnIndex=-1;
        int mobileColumnIndex=-1;
        List<ClientBank> clientBanks = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            ClientBank clientBank = new ClientBank();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
               if(row.getRowNum()==0){
                   if(cell.getStringCellValue().equalsIgnoreCase("Name")){
                       nameColumnIndex = cell.getColumnIndex();
                       continue;
                   }else if(cell.getStringCellValue().equalsIgnoreCase("Email")){
                       emailColumnIndex = cell.getColumnIndex();
                       continue;
                   }else if(cell.getStringCellValue().equalsIgnoreCase("Mobile")){
                       mobileColumnIndex = cell.getColumnIndex();
                       continue;
                   }
               }
                if(nameColumnIndex==cell.getColumnIndex()){
                    clientBank.setFullName(cell.getStringCellValue());
                    continue;
                }
                if(emailColumnIndex==cell.getColumnIndex()){
                    clientBank.setEmail(cell.getStringCellValue());
                    continue;
                }
                if(mobileColumnIndex==cell.getColumnIndex()){
                    clientBank.setMobileNo(cell.getStringCellValue());
                    continue;
                }
            }
            if(clientBank.getEmail()!=null){
                clientBanks.add(clientBank);
            }
            System.out.println(clientBank.getFullName() + " " + clientBank.getEmail() + " " + clientBank.getMobileNo());
        }
        clientBankRepository.saveAll(clientBanks);
        return "bulk-data-upload";
    }

    private File convertMultiPartToFile(MultipartFile file ) throws IOException {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }


}
