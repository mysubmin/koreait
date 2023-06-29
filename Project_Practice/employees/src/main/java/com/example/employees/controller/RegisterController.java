    private RegisterMapper reigsterMapper;

    @Autowired
    public RegisterController(RegisterMapper reigsterMapper) {
        this.reigsterMapper = reigsterMapper;
    }

    @GetMapping("/main/register")
    public String getRegister() {
        return "main/register";
    }

    @PostMapping("/main/register")
    @ResponseBody
    public List<DeptDto> getDept() {
        return reigsterMapper.getDept();
    }


    @PostMapping("/main/getPos")
    @ResponseBody
    public List<PosDto> getPos(@RequestParam String selDeptValue) {
        return reigsterMapper.getPos(selDeptValue);
    }

    @PostMapping("/main/emailCheck")
    @ResponseBody
    public int emailCheck(@RequestParam String email) {
        return reigsterMapper.emailCheck(email);
    }
